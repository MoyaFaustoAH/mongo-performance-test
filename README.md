# MongoDB Bulk Insert vs. saveAll Performance Test

This is a small test application to compare the performance of `MongoRepository.saveAll()` method against
`MongoTemplate.bulkOps()`.

> [!IMPORTANT]  
> This is not a rigorous benchmark, but it provides a rough comparison between the two approaches.
> Results may vary depending on your machine's performance, but generally, `MongoTemplate.bulkOps()` is significantly
> faster than
> `MongoRepository.saveAll()`.

> [!TIP]
> Check the container's CPU usage during the tests. You'll notice a high usage during the `MongoRepository.saveAll()`
> test execution

## How to Use

1. Clone the repository.
2. Run the tests with:

```shell
./gradlew test
```

## What It Does

- The tests generate a large number of documents and measure the time taken to insert them using both `saveAll` and
  BulkOps `insert`.
- The test duration includes the time to generate the documents, but assertions only consider the time taken to save
  them to
  MongoDB.

- You can check the CPU usage of the container while executing 
