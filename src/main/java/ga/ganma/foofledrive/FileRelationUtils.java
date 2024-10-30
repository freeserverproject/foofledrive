package ga.ganma.foofledrive;

import ga.ganma.foofledrive.playerdata.Playerdata;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Filerelationクラスは、プレイヤーデータのファイル操作を行うためのユーティリティクラスです。
 * プレイヤーデータの読み書きや存在確認を行います。
 */
public final class FileRelationUtils {

    public static final Path PLAYERDATA_FOLDER = new File(Foofledrive.ender.getDataFolder() + File.separator + "playerdata").toPath();
    public static final String DATA_FILE_EXTENTION = ".dat";

    /**
     * インスタンス化を禁止するためのプライベートコンストラクタ。
     */
    private FileRelationUtils() {}
    /**
     * 指定されたプレイヤーのデータファイルが存在するかを確認します。
     *
     * @param p チェックするプレイヤー
     * @return データファイルが存在する場合はtrue、存在しない場合はfalse
     */
    public static boolean nameCheck(Player p) {
        return new File(PLAYERDATA_FOLDER.toString() + p.getUniqueId() + ".dat").exists();
    }

    /**
     * 指定されたオフラインプレイヤーのデータファイルが存在するかを確認します。
     *
     * @param offp チェックするオフラインプレイヤー
     * @return データファイルが存在する場合はtrue、存在しない場合はfalse
     */
    public static boolean nameCheck(OfflinePlayer offp) {
        return new File(PLAYERDATA_FOLDER.toString() + offp.getUniqueId() + ".dat").exists();
    }

    public static boolean nameCheck(UUID uuid) {
        return new File(PLAYERDATA_FOLDER.toString() + uuid + ".dat").exists();
    }

    /**
     * 指定されたプレイヤーデータをファイルに書き込みます。
     * @deprecated このメソッドの新規の使用は推奨しません。
     * @param e 書き込むプレイヤーデータ
     */
    public static void createFile(Playerdata e) {
        try {
            File folder = PLAYERDATA_FOLDER.toFile();
            folder.mkdir();
            File file = new File(PLAYERDATA_FOLDER + e.getMcid().toString() + ".dat");

            if (file.exists()) {
                file.delete();
            }
            try (FileOutputStream fos = new FileOutputStream(file);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(e);
            }
        } catch (IOException io) {
            Foofledrive.ender.getLogger().log(Level.SEVERE, "ファイルの作成に失敗しました。");
        }
    }

    /**
     * 指定されたプレイヤーのデータファイルを読み込みます。
     *
     * @param p 読み込むプレイヤー
     * @return 読み込んだプレイヤーデータ、存在しない場合はnull
     */
    public static Playerdata readFile(Player p) {
        return readFileInternal(p.getUniqueId());
    }

    /**
     * 指定されたオフラインプレイヤーのデータファイルを読み込みます。
     *
     * @param offp 読み込むオフラインプレイヤー
     * @return 読み込んだプレイヤーデータ、存在しない場合はnull
     */
    public static Playerdata readFile(OfflinePlayer offp) {
        return readFileInternal(offp.getUniqueId());
    }

    /**
     * 内部的にプレイヤーデータファイルを読み込みます。
     *
     * @param uniqueId 読み込むプレイヤーのUUID
     * @return 読み込んだプレイヤーデータ、存在しない場合はnull
     */
    private static Playerdata readFileInternal(UUID uniqueId) {
        Playerdata pd = null;
        if (!nameCheck(uniqueId)) {
            return null;
        }
        try (FileInputStream fis = new FileInputStream(PLAYERDATA_FOLDER.toString() + uniqueId + ".dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            pd = (Playerdata) ois.readObject();
        } catch (IOException e) {
            Foofledrive.ender.getLogger().log(Level.SEVERE, "ファイルの読み取りに失敗しました。");
        } catch (ClassNotFoundException e) {
            Foofledrive.ender.getLogger().log(Level.SEVERE, "内部エラーが発生しました。");
        }
        return pd;
    }
}