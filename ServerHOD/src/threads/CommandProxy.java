package threads;

import java.util.concurrent.ForkJoinPool;

public class CommandProxy {
	
	private ForkJoinPool forkJoinPool = new ForkJoinPool();
	
	public void CommandToPool (CommandTask ct) {
		ct.fork();
	}
	
	
}
