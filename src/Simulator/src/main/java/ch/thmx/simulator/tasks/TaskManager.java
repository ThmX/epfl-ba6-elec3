package ch.thmx.simulator.tasks;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service("taskManager")
public class TaskManager extends Thread {
	
	private static class TaskWrapper {
		
		String name;
		Task task;
		
		public TaskWrapper(String name, Task task) {
			super();
			this.name = name;
			this.task = task;
		}
	}
	
	private static final Logger log = Logger.getLogger(TaskManager.class);
	
	private LinkedBlockingQueue<TaskWrapper> queue = new LinkedBlockingQueue<TaskWrapper>();

	private boolean active;
	
	// XXX HAAAAAAAAAAAAAAAACK
	public TaskManager() {
		init();
	}
	
	@PostConstruct
	public void init() {
		start();
	}
	
	@Override
	public void run() {
		TaskWrapper wrapper = null;

		log.info("Started TaskManager[" + getId() + "]...");

		this.active = true;
		while (this.active) {
			wrapper = extractTask();
			
			if (wrapper != null) {
				log.info("Launching task: " + wrapper.name);
				try {
					wrapper.task.run();				
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					log.info("Ended task: " + wrapper.name);
					
					synchronized (this) {
						notifyAll();
					}
				}
			}
		}
		log.info("Stopped TaskManager");
	}
	
	public void stopTaskManager() {
		this.active = false;
	}
	
	private synchronized TaskWrapper extractTask() {
		
		while (this.queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				log.error("", e);
			}				
		}
		
		return this.queue.poll();
	}
	
	public synchronized boolean addTask(String name, Task task) {
		if (task == null) {
			return false;
		}

		boolean offered = this.queue.offer(new TaskWrapper(name, task));
		
		synchronized (this) {
			notifyAll();
		}
		
		return offered;
		
	}
	
	public void clearTasks() {
		this.queue.clear();
	}
	
	public void waitForFinishingTasks() {
		synchronized (this) {
			while (!this.queue.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					log.error("", e);
				}				
			}
		}
	}
}

