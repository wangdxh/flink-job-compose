#!/usr/bin/python
#coding=utf-8
import os
import io
import re
import hashlib
curdirname = os.path.dirname(os.path.abspath(__file__))
print (curdirname)
jsonjavapath = os.path.join(curdirname, "flinketl/src/main/java/com/kedacom/flinketlgraph/json")
print (jsonjavapath)

for root,dirs,files in os.walk(jsonjavapath):
    for file in files:
        filefullpath = os.path.join(root,file)
        print(filefullpath)
        strfile = ""
        with io.open(filefullpath, 'r', encoding="utf-8") as f:
            strfile = f.read()
        classnames = re.findall(r'public class (.*) implements', strfile)
        serivalid = re.findall(r'final static long serialVersionUID = (.*)L;', strfile)
        if len(classnames)!=1 or len(serivalid)!=1:
            x = 1/0
        newid = int(hashlib.md5(classnames[0].encode("utf-8")).hexdigest()[0:10], 16)
        print (classnames[0], serivalid[0], newid)
        strfile = strfile.replace(serivalid[0], "%d" % newid)
        with io.open(filefullpath, 'w', encoding="utf-8") as f:
            f.write(strfile)
