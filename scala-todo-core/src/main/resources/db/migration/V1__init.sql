CREATE TABLE "TODOS" (
  "ID" BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  "DESCRIPTION" VARCHAR NOT NULL,
  "DONE" BOOLEAN DEFAULT false NOT NULL
)
