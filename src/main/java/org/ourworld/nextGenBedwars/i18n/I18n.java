package org.ourworld.nextGenBedwars.i18n;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.ourworld.nextGenBedwars.NextGenBedwars;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * 在config.yml种 language 项种配置
 */
public class I18n {
    //默认语言，要确保resource/languages/文件夹内有对应名配置
    public static String DEFAULT_LANGUAGE = "zh_CN";
    public static final File LANGUAGES_DIRECTORY = new File(NextGenBedwars.plugin.getDataFolder(), "languages");
    private static YamlConfiguration LOADED;

    public static void init() {
        checkFile();
        load();
    }

    /**
     * 检查文件
     * 如果language文件夹不存在时，生成默认语言
     */
    private static void checkFile() {
        if (!LANGUAGES_DIRECTORY.exists()) {
            LANGUAGES_DIRECTORY.mkdirs();
            //没有文件夹的时候是第一次运行，让腐竹可以删除不想要
            NextGenBedwars.plugin.saveResource("languages/zh_CN.yml", false);
            //...
        }
    }

    private static void load() {
        FileConfiguration config = NextGenBedwars.plugin.getConfig();
        String locale = config.getString("language", safeLanguage(DEFAULT_LANGUAGE));
        File file = new File(LANGUAGES_DIRECTORY, locale + ".yml");
        Logger logger = NextGenBedwars.plugin.getLogger();
        if (!file.exists()) {
            file = new File(LANGUAGES_DIRECTORY, DEFAULT_LANGUAGE + ".yml");
            if (!file.exists()) NextGenBedwars.plugin.saveResource("languages/zh_CN.yml", false);
            logger.warning("Language file not found, using default language: " + DEFAULT_LANGUAGE);
        }
        LOADED = YamlConfiguration.loadConfiguration(file);
        logger.info("Language file loaded: " + file.getName());
    }

    /**
     * 获取一个安全的语言
     *
     * @param safe 一项存在的语言
     * @return 语言文件夹内有本地环境对应语言的文件时返回本地语言，如果没有返回 {@code safe}
     */
    private static String safeLanguage(String safe) {
        Locale locale = Locale.getDefault();
        String s = locale.getLanguage() + "_" + locale.getCountry();
        if (new File(LANGUAGES_DIRECTORY, s + ".yml").exists()) return s;
        return safe;
    }

    /**
     * 翻译
     * <p>
     * translate.yml -> example: 'he{0}{0}o, w{1}ld'
     * <pre>
     *     key = 'example'
     *     I18n.translate(key,"l" ,"or") == 'hello, world'
     * </pre>
     * </p>
     * 按照键名的到语言文件内获取的内容后通过参数进行格式化
     * @param key  翻译键
     * @param args 可选参数
     * @return 返回翻译格式化后的字符
     */
    public static String translate(String key, Object... args) {
        String str = LOADED.getString(key);
        if (str == null) return key;
        return MessageFormat.format(key, args);
    }

    /**
     * 翻译列表
     * @see I18n#translate(String, Object...)
     */
    public static List<String> translateList(String key, Object... args) {
        List<String> list = LOADED.getStringList(key);
        if (list.isEmpty()) return list;
        list.replaceAll(s -> MessageFormat.format(s, args));
        return list;
    }
}
