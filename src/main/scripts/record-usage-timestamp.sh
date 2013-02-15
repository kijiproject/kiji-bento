#!/usr/bin/env bash
#
#   (c) Copyright 2013 WibiData, Inc.
#  
#   See the NOTICE file distributed with this work for additional
#   information regarding copyright ownership.
#  
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#  
#       http://www.apache.org/licenses/LICENSE-2.0
#  
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
# This script runs a Java program that writes the current time in milliseconds to 
# the file .kiji-last-used.

bin=`dirname $0`

# This script should be a part of a bin dir inside a Kiji BentoBox. 
# The kiji-bento lib dir should be one directory above.
kiji_bento_lib_dir="${bin}/../lib"

# We should log any problems with writing the timestamp to bento-cluster's state dir.
ts_log_file="${bin}/../cluster/state/bento-ts.log"

# Everything in the kiji-bento lib dir should go on the classpath.
tool_classpath="${kiji_bento_lib_dir}/*"

# Run the tool.
java -cp "${tool_classpath}" org.kiji.bento.box.tools.UsageTimestampTool &> "${ts_log_file}"
exit $?

