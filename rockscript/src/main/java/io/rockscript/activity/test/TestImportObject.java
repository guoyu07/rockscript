/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rockscript.activity.test;

import io.rockscript.engine.EngineException;
import io.rockscript.engine.ScriptService;
import io.rockscript.engine.StartScriptExecutionResponse;
import io.rockscript.activity.ActivityOutput;
import io.rockscript.activity.ImportObject;
import io.rockscript.activity.ImportProvider;

public class TestImportObject extends ImportObject implements ImportProvider {

  private TestResult testResult;
  private ScriptService scriptService;

  public TestImportObject(TestResult testResult) {
    super("rockscript.io/test");
    this.testResult = testResult;
    put("start", activityInput -> {
      String scriptName = activityInput.getArgProperty("scriptName");
      StartScriptExecutionResponse response = scriptService.newStartScriptExecutionCommand()
          .scriptName(scriptName)
          .execute();
      return ActivityOutput.endFunction(response.getScriptExecution());
    });
    put("assertEquals", activityInput -> {
      Object actual = activityInput.getArg(0);
      Object expected = activityInput.getArg(1);
      if (!equal(actual, expected)) {
        throw new EngineException("Expected "+expected+", but was "+actual, activityInput);
      }
      return ActivityOutput.endFunction();
    });
  }

  private boolean equal(Object a, Object b) {
    if (a==null && b==null) {
      return true;
    }
    if (a!=null) {
      return a.equals(b);
    }
    return b.equals(a);
  }

  public TestResult getTestResult() {
    return testResult;
  }

  @Override
  public ImportObject getImportObject() {
    return this;
  }

  public void setScriptService(ScriptService scriptService) {
    this.scriptService = scriptService;
  }
}