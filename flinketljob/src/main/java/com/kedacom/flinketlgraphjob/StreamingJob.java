/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kedacom.flinketlgraphjob;

import com.kedacom.flinketlgraph.FlinkJobFactory;
import com.kedacom.flinketlgraph.json.Jobtestrequest;
import com.kedacom.flinketlgraph.json.Jobtestresult;
import com.kedacom.flinketlgraph.minicluster.MyMiniCluster;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.util.StringUtils;


/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {

	public static void main(String[] args) throws Exception {
		/*ObjectMapper mapper1 = new ObjectMapper();
		RulerBean bean = new RulerBean("123", "xx", "sfdsfdsaf");
		String out1 = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(bean);
		System.out.println(out1);*/

		FlinkJobFactory.InitFlinkOperator();
		MyMiniCluster.initCluster();

		System.out.println("*****************************************************");

		try {
			ParameterTool tool = ParameterTool.fromArgs(args);
			String filepath = tool.get("jsonfile");
			if (StringUtils.isNullOrWhitespaceOnly(filepath)){
				throw new Exception("jsonfile parameters not set");
			}

			Jobtestrequest req = new Jobtestrequest();
			req.setDatanums(5L);
			req.setTimeout(60L);
			req.setJsonfile(filepath);
			Jobtestresult res = MyMiniCluster.testJob(req);
			ObjectMapper mapper = new ObjectMapper();
			String out = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res);
			System.out.println(out);
			System.out.println(res.getExceptionstack());

		}finally {
			MyMiniCluster.stopCluster();
		}
	}
}
