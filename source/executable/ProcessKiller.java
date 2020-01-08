package executable;

public class ProcessKiller extends Thread {

	Process process;
	Integer timeout;
	boolean running = false;

	ProcessKiller(Process process, Integer timeout) {
		this.process = process;
		this.timeout = timeout;
	}

	public void run() {
		if (timeout == null) {
			return;
		}
		running = true;
		long marker = System.currentTimeMillis() / 1000;
		long now;
		while (running && ((now = (System.currentTimeMillis() / 1000)) - marker) < timeout) {
			try {
				long sleep = ((marker + timeout - now) * 1000) + 500;
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				if (!running /* !process.isAlive() */) {
					// System.err.println(this.getClass().getCanonicalName() + " told to stop. Process most probably has exited normally.");
					return;
				}
			}
		}
		running = false;
		if (process.isAlive()) {
			System.err.println(this.getClass().getCanonicalName() + " Process is still alive after " + timeout + " seconds. Destroying it.");
			process.destroyForcibly();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}