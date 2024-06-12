# Energy Estimator

## Overview

This project is a command line tool that estimates the power draw of a single dimmable smart light bulb. The tool reads messages from standard input until it reaches an EOF and outputs the estimated energy usage in watt-hours.

## Building the Project

```sh
./gradlew build
```

## Running the Tests

```sh
./gradlew test
```

## Usage

```sh
$ java -cp out energy.estimator.EnergyEstimatorDemo
Enter messages (type 'EOF' to end input):
> 1544206562 TurnOff
> 1544206563 Delta +0.5
> 1544210163 TurnOff
> 1544210163 Usage 1544206562 1544210163
EOF
Energy usage from 1544206562 to 1544210163: 2.500 Wh
```

## Cases Not Handled

1. Messages Arriving in Bulk:
Problem: The current solution reads messages one by one from standard input and processes them. In a real-world scenario, messages might arrive in bulk or bursts.
Solution: Implement batch processing to handle multiple messages simultaneously. This could involve using a message queue system like RabbitMQ or Kafka to buffer and process messages in batches.

2. Handling Lost Messages:
Problem: The solution does not account for lost messages, which can lead to inaccuracies in energy estimation.
Solution: Implement a mechanism to detect and handle missing messages. This could involve using sequence numbers in messages to identify gaps and request retransmission of lost messages.

3. Duplicate Messages:
Problem: While the solution checks for duplicate messages, it does not currently log or provide feedback on the occurrence of duplicates.
Solution: Enhance the duplicate message handling by logging duplicates and potentially providing a feedback mechanism to the sender to avoid resending the same message.

4. Multiple Light Bulbs:
Problem: The current implementation assumes a single light bulb. In a real-world scenario, there may be multiple bulbs.
Solution: Extend the solution to handle multiple bulbs by including an identifier in each message to distinguish between different bulbs. Maintain separate state and processing for each bulb.

5. High Frequency of Messages:
Problem: If messages are sent at a very high frequency, the current solution may become inefficient.
Solution: Optimize the solution to handle high-frequency messages, potentially by using more efficient data structures or parallel processing.
