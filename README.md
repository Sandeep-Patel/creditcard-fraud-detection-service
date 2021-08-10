# Credit Card Fraud Detector Service

Service to detect if the credit card is fraud based on total amount for transactions in given time period
 
A credit card transaction has:
- hashed credit card number
- timestamp
- amount

Transactions are to be received as a comma separated string of elements in input file.

```
eg. c6c2ed18c1db490fa7d7e71125a4c168, 2021-08-07T13:35:49, 50
```

A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit card number
over a 24 hour sliding window period exceeds the price threshold given as input.

## Getting Started

Follow the instructions for running the project.  

### Prerequisites

1. JDK 8 or above

### Installing & Running

1. Checkout the code: ```git clone https://github.com/Sandeep-Patel/creditcard-fraud-detection-service.git```
2. Open terminal, go to `creditcard-fraud-detection-service`
3. Build: ```mvn clean install```
3. Run: ```java -jar target/creditcard-fraud-detection-service-1.0-SNAPSHOT.jar transactions 120```

## Test Data

Execute `TestDataGenerator.java` to generate test data in chronological order
 

