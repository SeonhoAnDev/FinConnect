# FinConnect

## プロジェクト概要
金融系SNSアプリケーションのバックエンド開発プロジェクトです。ユーザー同士が金融に関する情報を共有し、コミュニケーションを取ることができるプラットフォームを目指しています。

## 画面イメージ
[ここに画面のスクリーンショットを追加予定]

## 主な機能
### 実装済み機能
- **ユーザー管理システム**
  - 会員登録・ログイン機能
  - プロフィール編集
  - JWT認証による安全な認証システム
  
- **SNS基本機能**
  - 投稿の作成・編集・削除
  - コメント機能
  - いいね機能
  - フォロー/フォロワー機能

- **フロントエンド連携**
  - Reactで作成したフロントエンドとの完全な連携

### 開発予定の機能
- 金融関連ダッシュボード
- 経済ニュースフィード
- 独自の本人認証システム
- リアルタイムチャット機能
- イベント管理システム

## 使用技術

### バックエンド
- **メイン言語・フレームワーク**
  - Java
  - Spring Boot 3.x
  
- **データベース**
  - PostgreSQL
  - JPA/Hibernate

- **セキュリティ**
  - Spring Security
  - JWT（JSON Web Token）
  - BCrypt暗号化

### 開発ツール
- Postman（API テスト）
- Git（バージョン管理）

## システム構成図
src/
├── main/
│ ├── java/
│ │ └── com.finconnect/
│ │ ├── config/ # 設定ファイル
│ │ ├── controller/ # APIエンドポイント
│ │ ├── model/ # エンティティ
│ │ ├── repository/ # データアクセス
│ │ └── service/ # ビジネスロジック
└─└─── resources/


## 主要API一覧
### ユーザー関連
- `POST /api/v1/auth/signup` - 会員登録
- `POST /api/v1/auth/login` - ログイン
- `GET /api/v1/users/{id}` - ユーザー情報取得
- `PUT /api/v1/users/{id}` - ユーザー情報更新

### 投稿関連
- `GET /api/v1/posts` - 投稿一覧取得
- `POST /api/v1/posts` - 投稿作成
- `PUT /api/v1/posts/{id}` - 投稿更新
- `DELETE /api/v1/posts/{id}` - 投稿削除

### ソーシャル機能
- `POST /api/v1/follows/{userId}` - フォロー
- `DELETE /api/v1/follows/{userId}` - フォロー解除
- `GET /api/v1/users/{id}/followers` - フォロワー一覧
- `GET /api/v1/users/{id}/following` - フォロー中一覧

## 今後の展望
- クラウドプラットフォームへのデプロイ
- マイクロサービスアーキテクチャの採用検討
- パフォーマンス最適化
- セキュリティ強化

## 開発者
Seonho AN 
