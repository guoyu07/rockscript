/*
 * Copyright ©2017, RockScript.io. All rights reserved.
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

package io.rockscript.engine;

import java.util.List;

import io.rockscript.Engine;

public abstract class EngineImpl implements Engine {

  protected EngineConfiguration engineConfiguration;

  public EngineImpl(EngineConfiguration engineConfiguration) {
    this.engineConfiguration = engineConfiguration;
    this.engineConfiguration.throwIfNotProperlyConfigured();
  }

  public Script deployScript(String scriptText) {
    String scriptId = engineConfiguration.getScriptIdGenerator().createId();
    Script script = parseScript(scriptText);
    script.setId(scriptId);
    storeScript(script, scriptText);
    engineConfiguration
      .getEventStore()
      .handle(new ScriptDeployedEvent(script, scriptText));
    return script;
  }

  protected Script parseScript(String scriptText) {
    Script script = Parse.parse(scriptText);
    script.setEngineConfiguration(engineConfiguration);
    return script;
  }

  private void storeScript(Script script, String scriptText) {
    engineConfiguration
      .getScriptStore()
      .saveScript(script, scriptText);
  }

  public ScriptExecution startScriptExecution(String scriptId) {
    return startScriptExecution(scriptId, null);
  }

  public ScriptExecution startScriptExecution(String scriptId, Object input) {
    Script script = engineConfiguration
      .getScriptStore()
      .findScriptById(scriptId);

    String scriptExecutionId = engineConfiguration
        .getScriptExecutionIdGenerator()
        .createId();

    ScriptExecution scriptExecution = new ScriptExecution(scriptExecutionId, engineConfiguration, script);
    scriptExecution.setInput(input);

    ScriptStartedEvent scriptStartedEvent = new ScriptStartedEvent(scriptExecution, input);
    scriptExecution.dispatch(scriptStartedEvent);

    engineConfiguration
      .getLockService()
      .newScriptExecution(scriptExecution, "localhost");

    scriptExecution.start();

    return scriptExecution;
  }

  @Override
  public ScriptExecution endWaitingAction(String scriptExecutionId, String executionId) {
    return endWaitingAction(scriptExecutionId, executionId, null);
  }

  @Override
  public ScriptExecution endWaitingAction(String scriptExecutionId, String executionId, Object result) {
    ScriptExecution scriptExecution = engineConfiguration
      .getEventStore()
      .findScriptExecutionById(scriptExecutionId);

    ArgumentsExpressionExecution execution = (ArgumentsExpressionExecution) scriptExecution
      .findExecutionRecursive(executionId);

    execution.endAction(result);

    return scriptExecution;
  }

  public EngineConfiguration getEngineConfiguration() {
    return engineConfiguration;
  }

  @Override
  public List<ScriptExecution> recoverCrashedScriptExecutions() {
    return engineConfiguration
      .getEventStore()
      .recoverCrashedScriptExecutions();
  }
}
