#!/bin/bash
source colors.sh
MYSQL=mysql
MYSQL_ARGS='--user=happs'
MYSQL_TABLE=happs
OUTPUT=output.tab
OUTPUT_ERR=output_err.tab

function exec_mysql() {
	if [[ ! -f $1 ]]; then
		red file not found $1
	fi

	${MYSQL} ${MYSQL_ARGS} ${MYSQL_TABLE} < $1 >> ${OUTPUT} 2> ${OUTPUT_ERR}

	if [ $? -ne 0 ]; then
		yellow "error processing file $1"
		red $(cat ${OUTPUT_ERR})
		exit
	fi
}

echo "$(eval date): init tables" > ${OUTPUT}

exec_mysql usertable.sql
exec_mysql rostertable.sql
