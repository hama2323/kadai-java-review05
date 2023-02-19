package jp.co.kiramex.dbSample.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement; // ← 修正

public class Review05 {

    public static void main(String[] args) {
        // 3. データベース接続と結果取得のための変数宣言
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null; // ← 修正

        try {
            // 1. ドライバのクラスをJava上で読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. DBと接続する
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/kadaidb?useSSL=false&allowPublicKeyRetrieval=true", // ←kadaidbデータベースに接続
                    "root", "Hk0713196");

            // 4. DBとやりとりする窓口（Statementオブジェクト）の作成
            stmt = con.createStatement();

            // 5, 6. Select文の実行と結果を格納／代入
            System.out.print("検索キーワードを入力してください ＞ "); // personテーブルからidを条件に1件のデータを取得するプログラムを作成
            String input = keyIn(); // idはキーボード入力を受け付ける
            String sql = "SELECT * FROM person WHERE id = ?";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, input);

            rs = pstmt.executeQuery();

            // 7. 結果を表示する
            while (rs.next()) {
                // Name列の値を取得
                String name = rs.getString("Name");
                // Population列の値を取得 ← 追記
                int age = rs.getInt("Age");

                // 取得した値を表示
                System.out.println(name);
                System.out.println(age);
            }

        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            // 8. 接続を閉じる
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("ResultSetを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Statementを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("データベース切断時にエラーが発生しました。");
                    e.printStackTrace();
                }
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        System.err.println("PreparedStatementを閉じるときにエラーが発生しました。");
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static String keyIn() {
        String line = null;
        try {
            BufferedReader key = new BufferedReader(new InputStreamReader(System.in));
            line = key.readLine();
        } catch (IOException e) {

        }
        return line;
    }
}