package executable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import orion.core.Utility;

public class Executable implements Runnable {

	protected boolean alive = true;
	protected Integer statusCode;
	protected List<String> logList;

	protected Process process;
	protected StreamGobbler outputGobbler;
	protected StreamGobbler errorGobbler;
	protected ProcessKiller processKiller;

	protected String[] command;
	protected String dataPath;
	protected String workingPath;
	protected Integer timeout;
	protected Map<String, String> environmentMap;

	public Executable(List<String> logList, String[] command, String dataPath, String workingPath, Integer timeout, Map<String, String> environmentMap) {
		this.logList = logList;
		this.command = command;
		this.dataPath = dataPath;
		this.workingPath = workingPath;
		this.timeout = timeout;
		this.environmentMap = environmentMap;
	}

	public void run() {
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(workingPath));

		if (environmentMap != null) {
			for (Map.Entry<String, String> entry : environmentMap.entrySet()) {
				processBuilder.environment().put(entry.getKey(), entry.getValue());
			}
		}

		try {
			process = processBuilder.start();
			// System.out.println(Thread.currentThread() + " Executing " + Arrays.asList(command));
			outputGobbler = new StreamGobbler(process.getInputStream(), logList);
			errorGobbler = new StreamGobbler(process.getErrorStream(), logList);
			processKiller = new ProcessKiller(process, timeout);
			outputGobbler.start();
			errorGobbler.start();
			processKiller.start();
			statusCode = process.waitFor();
			if (processKiller.isRunning() /* processKiller.isAlive() */) {
				processKiller.setRunning(false);
				processKiller.interrupt();
			}
			outputGobbler.join();
			errorGobbler.join();
		} catch (IOException | InterruptedException e) {
			Utility.slurp(logList, e);
		}
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

}