# scala-todo

![Travis CI status](https://travis-ci.org/nacam403/scala-todo.svg?branch=master)

Scalaの練習用に作ったTodoアプリ。

以下のサブプロジェクトから構成される。

- コア
- REST API
- フロントエンド

以下のライブラリ、フレームワークを使う。
それぞれの分野における定番どころということで、基本的な使い方を身につけるために選んだ。

- DBアクセスにはSlick
- テスティングフレームワークにはScalaTest
- WebアプリのフレームワークにはPlay Framework

## コア

DBアクセスを担当する。コアはREST APIから呼ばれる。

DBは、Todoを管理するテーブルが一つあるだけ。
カラムは以下の通り。

- id: 連番
- description: Todoの内容を表す文字列
- done: Todoの完了/未了を表すBoolean

DBスキーマ作成を自動化するために、Flywayによるマイグレーションを行う（といってもテーブルを1個作るだけ）。

## REST API

JSON形式のAPIをフロントエンドに提供するPlay Frameworkプロジェクト。
具体的な処理はコアに委譲し、自分はJSONとScalaオブジェクトの変換などをするだけ。

エンドポイントは以下の通り。

- GET /todos Todoの一覧を返す。
- POST /todos Todoを新規作成する。
- GET /todos/:id 指定されたTodoを返す。
- PUT /todos/:id 指定されたTodoを更新する。
- DELETE /todos/:id 指定されたTodoを削除する。

## フロントエンド

Gitのサブモジュールとして https://github.com/nacam403/react-todo-frontend を内包しているPlay Frameworkプロジェクト。
それだけといえばそれだけ。

react-todo-frontendからコードを取り込んで、npmのコマンドでJavaScriptなどをビルドする。
ビルド生成物はfrontend/distディレクトリに出力される。
このディレクトリは、sbtのunmanagedResourceDirectoriesに追加されており、JavaScriptなどのファイルが公開される。

## デプロイの想定

REST APIとフロントエンドを別々のPlay Frameworkプロジェクトとしたので、異なるポートで起動される。
そのままだとブラウザのSame-Originポリシーに引っかかってしまって、フロントエンドからREST APIにAjax通信ができない。
そのため、アプリケーションサーバーの前段にWebサーバー（nginxなど）がある構成で、例えば以下の様にデプロイされることを想定する。

- REST APIは9000番ポートで起動する。
- フロントエンドは9001番ポートで起動する。
- Webサーバーは80番ポートで起動する。
- 80番ポートの/rest/以下を9000番ポートに、/frontend/以下を9001番ポートに繋ぐ。
- ブラウザは、80番ポートにアクセスしてフロントエンドを利用する。
- production用にビルドされたフロントエンドでは、/rest/todos... というURLでAjax呼び出しをする。
