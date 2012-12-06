#!/usr/bin/env bash
#
#   (c) Copyright 2012 WibiData, Inc.
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
#   The kiji-env.sh script configures your environment to use kiji and the HDFS,
#   MapReduce, and HBase clusters started by the bento-cluster included with
#   kiji-bento. Use:
#
#   bash> source $KIJI_HOME/bin/kiji-env.sh
#
#   to configure your environment. The environment variables set are:
#
#   KIJI_HOME           Set to the parent of the directory this script is contained in.
#                       This should be the root of a kiji-bento distribution.
#
#   BENTO_CLUSTER_HOME  Set to the $KIJI_HOME/cluster directory, which should contain a
#                       bento-cluster distribution.
#
#   HADOOP_HOME         Set to the directory of the Hadoop distribution included with
#                       bento-cluster. This distribution should be located in
#                       $BENTO_CLUSTER_HOME/lib.
#
#   HBASE_HOME          Set to the directory of the HBase distribution included with
#                       bento-cluster. This distribution should be located in
#                       $BENTO_CLUSTER_HOME/lib.
#
#   PATH                The $PATH is modified so that $BENTO_CLUSTER_HOME/bin,
#                       $HADOOP_HOME/bin, $HBASE_HOME/bin, $KIJI_HOME/bin and
#                       $KIJI_HOME/schema-shell/bin are on it.
#

# Get the directory this script is located in, no matter how the script is being
# run.
bin="$( cd "$( dirname "${BASH_SOURCE:-$0}" )" && pwd )"

# The script is inside a kiji-bento distribution.
KIJI_HOME="${bin}/.."
export KIJI_HOME
echo "Set KIJI_HOME=${KIJI_HOME}"

PATH="${KIJI_HOME}/bin:${KIJI_HOME}/schema-shell/bin:${PATH}"
export PATH
echo "Added kiji and kiji-schema-shell binaries to PATH."

# Source the bento-env.sh script to configure the rest of the environment.
source "${KIJI_HOME}/cluster/bin/bento-env.sh"

