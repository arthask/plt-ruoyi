#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../sql/init ./mysql/db

# copy html
echo "begin copy html "
cp -r ../ruoyi-ui/dist/** ./nginx/dist


# copy jar
echo "begin copy plt-admin "
cp ../ruoyi-admin/target/ruoyi-admin.jar ./plt-admin/jar
