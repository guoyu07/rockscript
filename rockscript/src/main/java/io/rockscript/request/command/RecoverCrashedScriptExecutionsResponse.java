package io.rockscript.request.command;

import io.rockscript.engine.ScriptExecution;
import io.rockscript.request.CommandResponse;

import java.util.List;

public class RecoverCrashedScriptExecutionsResponse implements CommandResponse {

  public List<ScriptExecution> scriptExecutions;

  public RecoverCrashedScriptExecutionsResponse() {
  }

  public RecoverCrashedScriptExecutionsResponse(List<ScriptExecution> scriptExecutions) {
    this.scriptExecutions = scriptExecutions;
  }

  @Override
  public int getStatus() {
    return 200;
  }

  public List<ScriptExecution> getScriptExecutions() {
    return scriptExecutions;
  }
}
