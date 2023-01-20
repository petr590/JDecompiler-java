#!/usr/bin/python3
import re, subprocess

path = 'src/x590/jdecompiler/main/Version.java'

with open(path, 'r', encoding='utf-8') as file:
	content = file.read()
	found = re.search(r'(String\s+VERSION\s+=\s+")(\d+\.\d+\.)(\d+)(")', content)
	version = found.group(2) + str(int(found.group(3)) + 1)
	print('Program version updated: ' + version)

with open(path, 'w') as file:
	file.write(content[:found.start()])
	file.write(found.group(1))
	file.write(version)
	file.write(found.group(4))
	file.write(content[found.end():])

subprocess.run(['git', 'add', path])
