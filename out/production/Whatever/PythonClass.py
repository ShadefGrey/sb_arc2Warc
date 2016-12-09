#!/usr/bin/env python
'''Module to pack and unpack files in a directroy structure'''

import os
import md5
import anydbm
import stat
import sys

reload(sys)
sys.setdefaultencoding('utf8')

magic_string = 'SB web archive v 1.0\n'

class create_archive:
    def __init__(self, archive_name):
        self.file = open(archive_name, 'wb')
        self.file.write(magic_string)
        self.md5_dir = {}
        self.inode_dir = {}
        self.dir = []
        self.open = 1

    def __del__(self):
        self.close()

    def close(self):
        if self.open:
            dir = ''.join(self.dir)
            self.file.write( dir )
            self.file.write("%d\n" % len(dir))
            self.file.close()
            self.open = 0

    def add_dir(self, dir, level = 0):
        file_names = os.listdir(dir)
        file_names.sort()
        dirs = []
        for file_name in file_names:
            full_file_name = os.path.join(dir, file_name)
            a_file_name = file_name.replace('\n', '/')
            if os.path.isdir(full_file_name):
                dirs.append((full_file_name, a_file_name))
            else:
                self.add_file(full_file_name, a_file_name)
        for (full_file_name, a_file_name) in dirs:
            self.dir.append('/%d %s\n' % (level, a_file_name))
            self.add_dir(full_file_name, level + 1)

    def add_file(self, file_name, a_file_name):
        file_state = os.stat(file_name)
        inode = file_state[stat.ST_DEV], file_state[stat.ST_INO]
        if self.inode_dir.has_key(inode):
            check_sum = self.inode_dir[inode]
        else:
            fh = open(file_name, 'rb')
            content = fh.read()
            fh.close()
            check_sum = md5.md5(content).digest()
            self.inode_dir[inode] = check_sum
        if self.md5_dir.has_key(check_sum):
            pos = self.md5_dir[check_sum]
        else:
            pos = self.file.tell()
            self.md5_dir[check_sum] = pos
            head = "%d\n" % len(content)
            self.file.write(head)
            self.file.write(content)
        self.dir.append( '%d %s\n' % (pos, a_file_name) )

class read_archive:
    def __init__(self, archive_name, cache = None):
        self.file = open(archive_name, 'rb') # rw to lock file not write
        self.cache = cache
        self.names = []
        self.dir_is_read = 0
        if cache:
            #lock_file = open(self.cache+'.lock', 'w+')
            #fcntl.lockf(lock_file.fileno(), fcntl.LOCK_EX)
            try:
                self.dir = anydbm.open(cache, 'r')
                self.dir_is_read = 1
            except:
                self.create_cache()
                self.dir = anydbm.open(cache, 'r')
                #fcntl.lockf(lock_file.fileno(), fcntl.LOCK_UN)
                #lock_file.close()
        else:
            self.dir = {}
        self.open = 1

    def __del__(self):
        self.close()

    def close(self):
        if self.open:
            self.file.close()
            if self.cache:
                self.dir.close()
                self.cache = None
            self.open = 0

    def unpack(self, dir, hard_link = 0):
        if self.cache:
            self.cache = None
            self.dir.close()
            self.dir = {}
            self.dir_is_read = 0
        os.mkdir(dir)
        pos_to_file = {}
        self.read_dir()
        for file_name in self.names:
            pos = self.dir[file_name]
            full_file_name = os.path.join(dir, file_name.replace('?','__').replace(':','--').replace('\AUX','\AUX1').replace('...','@@@')) #TODO added recplace on "?"
            if pos == '/': # dir
                os.mkdir(full_file_name)
            else:
                if pos_to_file.has_key(pos) and hard_link:
                    os.link(pos_to_file[pos], full_file_name)
                else:
                    fh = open(full_file_name, 'wb') #TODO should be replaced with something I can actually reverse
                    fh.write(self.extract_data(pos))
                    fh.close()
                    pos_to_file[pos] = full_file_name


    def seek_to_dir(self):
        self.file.seek(0,2)				# end of file
        arch_length = self.file.tell()
        self.file.seek( - min(arch_length, 40), 2)	# go back
        tail = self.file.read()
        last_line = tail.split('\n')[-2]
        self.file.seek(- (len(last_line) + long(last_line) + 1), 2)

    def read_dir(self):
        if self.dir_is_read:
            return
        self.seek_to_dir()
        level = 0
        dirs = ()
        self.names = []
        while 1:
            line = self.file.readline()[:-1]
            line = line.split(' ')
            if len(line) == 1:
                break
            name = ' '.join(line[1:]).replace('/', '\n')
            if line[0][0] == '/':
                pos = '/'
                level = long(line[0][1:])
                dirs = dirs[:level] + (name,)
                path = os.path.join(*dirs)
            else:
                pos = long(line[0])
                path = os.path.join(*(dirs + (name,)))
            self.dir[path] = str(pos)
            self.names.append(path)
        self.dir_is_read = 1

    def list(self):
        self.read_dir()
        for name in self.names:
            if self.dir[name] == '/':
                print name+'/'
            else:
                print name

    def extract_file(self, file_name):
        self.read_dir()
        pos = self.dir[file_name]
        if pos == '/':
            return None
        return self.extract_data(pos)

    def extract_data(self, pos):
        self.file.seek(long(pos), 0)
        length = long(self.file.readline()[:-1])
        return self.file.read(length)

    def create_cache(self):
        self.dir = anydbm.open(self.cache, 'n')
        self.read_dir()

def test():
    os.system('rm -rf test test2 test3')
    os.mkdir('test')
    open('test/x1', 'w').write('xxx\n')
    open('test/x2', 'w').write('xxx\n')
    open('test/x3', 'w').write('xxx\n')
    os.mkdir('test/yy')
    open('test/yy/x1', 'w').write('xxy\n')
    open('test/yy/x2', 'w').write('xxz\n')
    open('test/yy/x3', 'w').write('xxv\n')
    a = create_archive('test.a')
    a.add_dir('test')
    a.close()
    a=read_archive('test.a')
    a.unpack('test2')
    a.unpack('test3', hard_link = 1)
    a.close()
    os.system('diff -r test test2')
    a=read_archive('test.a', 'test/a')
    print a.extract_file('x1')
    print a.extract_file('yy/x1')
    a.close()
    os.system('ls -l test test3')
    os.system('rm -rf test test2 test3')

def usage():
    print 'usage:'
    print '\t%s -pack ARCHIVE DIR_TO_PACK' % sys.argv[0]
    print '\t%s -[h]unpack ARCHIVE DIR_TO_MAKE' % sys.argv[0]
    print '\t%s -extract ARCHIVE FILE_NAME [CACHE]' % sys.argv[0]
    print '\t%s -list ARCHIVE' % sys.argv[0]
    sys.exit(1)

def main():
    if len(sys.argv) < 2:
        usage()
    if sys.argv[1] == '-pack':
        if len(sys.argv) != 4:
            usage()
        a = create_archive(sys.argv[2])
        a.add_dir(sys.argv[3])
        a.close()
    elif sys.argv[1] in ['-unpack', '-hunpack']:
        if len(sys.argv) != 4:
            usage()
        a = read_archive(sys.argv[2])
        a.unpack(sys.argv[3], hard_link = (sys.argv == '-hunpack'))
        a.close()
        print 'done' #TODO says when done
    elif sys.argv[1] == '-extract':
        if len(sys.argv) not in (4,5):
            usage()
        if len(sys.argv) == 5:
            a = read_archive(sys.argv[2], sys.argv[4])
        else:
            a = read_archive(sys.argv[2])
        sys.stdout.write(a.extract_file(sys.argv[3]))
        a.close()
    elif sys.argv[1] == '-list':
        if len(sys.argv) != 3:
            usage()
        a = read_archive(sys.argv[2])
        a.list()
        a.close()
    else:
        usage()
if __name__ == '__main__':
    main()
