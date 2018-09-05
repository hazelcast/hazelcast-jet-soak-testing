/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
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

package tests.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import java.util.concurrent.atomic.AtomicLong;

import static com.hazelcast.jet.impl.util.Util.uncheckRun;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class JmsMessageConsumer {

    private static final int SLEEP_MILLIS = 100;

    private final Thread consumerThread;
    private final String brokerURL;
    private final String queueName;
    private final AtomicLong count;

    private volatile boolean running = true;

    public JmsMessageConsumer(String brokerURL, String queueName) {
        this.consumerThread = new Thread(() -> uncheckRun(this::run));
        this.brokerURL = brokerURL;
        this.queueName = queueName;
        this.count = new AtomicLong();
    }

    private void run() throws Exception {
        Connection connection = new ActiveMQConnectionFactory(brokerURL).createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));

        while (running) {
            Message message = consumer.receiveNoWait();
            if (message == null) {
                MILLISECONDS.sleep(SLEEP_MILLIS);
            } else {
                count.incrementAndGet();
            }
        }
        consumer.close();
        session.close();
        connection.close();
    }

    public void start() {
        consumerThread.start();
    }

    public void stop() throws InterruptedException {
        running = false;
        consumerThread.join();
    }

    public long getCount() {
        return count.get();
    }


}
