package jp.co.kiramex.dbSample.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectSample02 {

    public static void main(String[] args) {
        // 3. データベース接続と結果取得のための変数宣言
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. ドライバのクラスをJava上で読み込む
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2. DBと接続する
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "Hk0713196"
                );
            // 4. DBとやりとりする窓口（Statementオブジェクト）の作成
            stmt = con.createStatement();
            
            // 5, 6. Select文の実行と結果を格納／代入
            String sql = "SELECT * FROM country where code = 'JPN'";
            rs = stmt.executeQuery(sql);
            
            // 7. 結果を表示する
            while( rs.next() ){
                // Name列の値を取得
                String name = rs.getString("Name");
                // Population列の値を取得 　← 追記
                int population = rs.getInt("Population");  // ← 追記
                // 取得した値を表示
                System.out.println(name + "\n" + population);
            }
            
            // 7-2. 更新処理を行う
            System.out.println("更新処理実行=============");
            String updateSql = "update country set Population = 1050000 where Code = 'JPN'";
            int count = stmt.executeUpdate(updateSql);
            System.out.println("更新行数：" + count);

            // 7-3. 更新後の結果を表示する
            rs.close();// 更新後の検索のため、一旦閉じる（閉じないと警告が出るため）
            System.out.println("更新後=================");
            rs = stmt.executeQuery(sql);
            if( rs.next() ){
                // Name列の値を取得
                String name = rs.getString("Name");
                // Population列の値を取得
                int population = rs.getInt("Population");
                // 取得した値を表示
                System.out.println(name + "\n" + population);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバのロードに失敗しました。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("データベースに異常が発生しました。");
            e.printStackTrace();
        } finally {
            
            // 8. 接続を閉じる
            if( rs != null ){
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("ResultSetを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if( stmt != null ){
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Statementを閉じるときにエラーが発生しました。");
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("データベース切断時にエラーが発生しました。");
                    e.printStackTrace();
                }
            }
        }
    }

}