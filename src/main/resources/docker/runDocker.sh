docker run --name finConnet-postgres \
-e POSTGRES_INITDB_ARGS="--data-checksums -E UTF8" \
-e POSTGRES_USER=finConnet-master \
-e POSTGRES_PASSWORD=root \
-e POSTGRES_DB=finConnet-db \
-e TZ=Asia/Tokyo \
-v $HOME/Desktop/javaProject/postgres/finConnet-data:/var/lib/postgresql/data \
-p 5432:5432 \
-d postgres:latest

