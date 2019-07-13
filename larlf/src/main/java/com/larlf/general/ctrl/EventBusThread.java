package com.larlf.general.ctrl;

import java.util.Calendar;

import org.apache.log4j.Logger;

public class EventBusThread extends Thread {
	private Logger log = Logger.getLogger(this.getClass());
	private long oldTime;
	private boolean isContinue = true;

	public EventBusThread() {
		// 初始化旧的时间
		this.oldTime = Calendar.getInstance().getTimeInMillis() / 1000 - 1;
	}

	/**
	 * 线程入口
	 */
	public void run() {
		while (isContinue) {
			// 对事件表加锁
			if (EventBus.eventLock.tryLock()) {
				try {
					// 取当前时间
					long t = Calendar.getInstance().getTimeInMillis() / 1000;

					// 如果间隔大于秒级
					if (this.oldTime < t) {
						// 遍历中间所有的秒
						for (long i = this.oldTime + 1; i <= t; ++i) {
							// 对定时事件进行检查
							for (int j = 0; j < EventBus.events.size(); ++j) {
								EventRule event = EventBus.events.get(j);
								if (event.check(t))
									event.execute(i);
							}
						}

						this.oldTime = t;
					}
				} catch (Exception e) {
					log.error("Check timer error", e);
				} finally {
					EventBus.eventLock.unlock();
				}
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				log.error("Sleep error", e);
			}
		}

	}

	public void stopThread() {
		this.isContinue = false;
	}
}
