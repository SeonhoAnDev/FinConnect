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
  - Spring Boot 3.4.1
  
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
```
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
```

## 主要API一覧

### 認証関連
- `POST /api/v1/auth/signup` - 会員登録
- `POST /api/v1/auth/login` - ログイン

### ユーザー関連
- `GET /api/v1/users` - ユーザー検索
- `GET /api/v1/users/{username}` - ユーザー情報取得
- `PATCH /api/v1/users/{username}` - ユーザー情報更新

### 投稿関連
- `GET /api/v1/posts` - 投稿一覧取得
- `POST /api/v1/posts` - 投稿作成
- `PATCH /api/v1/posts/{postId}` - 投稿更新
- `DELETE /api/v1/posts/{postId}` - 投稿削除

### 返信関連
- `GET /api/v1/posts/{postId}/replies` - 返信一覧取得
- `POST /api/v1/posts/{postId}/replies` - 返信作成
- `PATCH /api/v1/posts/{postId}/replies/{replyId}` - 返信更新
- `DELETE /api/v1/posts/{postId}/replies/{replyId}` - 返信削除

### ソーシャル機能
- `POST /api/v1/users/{username}/follow` - フォロー
- `DELETE /api/v1/users/{username}/follow` - フォロー解除
- `GET /api/v1/users/{username}/followers` - フォロワー一覧
- `GET /api/v1/users/{username}/following` - フォロー中一覧

### いいね機能
- `POST /api/v1/posts/{postId}/like` - いいね切り替え
- `GET /api/v1/posts/{postId}/likes` - いいねユーザー一覧

### 認証
すべてのAPI（認証系を除く）は、以下の形式でJWTトークンが必要：
Authorization: Bearer {access_token}

### エラーレスポンス
- 400: Bad Request
- 401: Unauthorized 
- 403: Forbidden
- 404: Not Found
- 500: Internal Server Error

## 今後の展望

### インフラストラクチャ
- AWSクラウドプラットフォームへのデプロイ予定
- パフォーマンス最適化
- セキュリティ強化

### 新規機能追加予定
#### コミュニケーション機能の拡張
- リアルタイムチャット機能
- グループチャット機能

#### 金融情報ダッシュボード
- リアルタイム金融ニュース配信
- 主要な金融指標のビジュアル化
- カスタマイズ可能なウィジェット

#### コミュニティ管理機能
- 金融関連の勉強会グループ作成
- イベント管理機能
- グループ別の掲示板機能

#### プラットフォーム内通貨システム
- プラットフォーム内で使用可能な独自通貨
- コミュニティ活動による通貨獲得
- ユーザー間での取引可能
- 投資シミュレーションへの活用
- コミュニティ内での経済活動促進
- イベント参加やコンテンツ購入に使用可能

## 開発者
Seonho AN 
