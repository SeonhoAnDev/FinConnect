docker run --name finConnect-postgres \
-e POSTGRES_INITDB_ARGS="--data-checksums -E UTF8" \
-e POSTGRES_USER=finConnect-master \
-e POSTGRES_PASSWORD=root \
-e POSTGRES_DB=finConnect-db \
-e TZ=Asia/Tokyo \
-v $HOME/Desktop/javaProject/postgres/finConnect-data:/var/lib/postgresql/data \
-p 5432:5432 \
-d postgres:latest

