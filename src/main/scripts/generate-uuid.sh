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
#   This script allows developers to run the bento script in the development distribution built 
#   under the project's target directory. If no built development distribution exists, maven
#   will be used to build one.
#
# This script runs a Java program that randomly generates a UUID and writes that UUID to
# the file .kiji-bento-uuid, if the file does not already exist.

bin=`dirname $0`

# This script should be a part of a bento-cluster inside a Kiji BentoBox. 
# The kiji-bento lib dir should be two directories above.
kiji_bento_lib_dir="${bin}/../../lib"

# We should log any problems with UUID generation to a file in bento-cluster's state dir.
uuid_log_file="${bin}/../state/bento-uuid.log"

# Everything in the kiji-bento lib dir should go on the classpath.
tool_classpath="${kiji_bento_lib_dir}/*"

# Run the tool.
java -cp "${tool_classpath}" org.kiji.bento.box.tools.UUIDGenerationTool &> "${uuid_log_file}"
exit $?

