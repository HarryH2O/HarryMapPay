package com.harrymappay;

import org.bukkit.plugin.java.JavaPlugin;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HarryMapPay extends JavaPlugin {
    private final String QR_CODE_NAME = "pay";
    private File qrCodeFolder;
    private File paymentHistoryFile;
    private File paymentHistoryBackupsFolder;
    private final Map<Player, Long> paymentTimeouts = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("插件 HarryMapPay v1.0.0 启用成功");

        // 检查配置文件
        saveDefaultConfig();

        // 加载配置文件
        reloadConfig();

        // 创建二维码文件夹
        qrCodeFolder = new File(getDataFolder(), "QuickResponse");
        qrCodeFolder.mkdirs();

        // 创建支付历史文件夹
        paymentHistoryFile = new File(getDataFolder(), "paymenthistory.csv");
        createFile(paymentHistoryFile);

        // 创建支付历史备份文件夹
        paymentHistoryBackupsFolder = new File(getDataFolder(), "paymenthistory_backups");
        paymentHistoryBackupsFolder.mkdirs();

        // 定时备份支付历史记录
        getServer().getScheduler().runTaskTimer(this, this::backupPaymentHistory, 0L, 30 * 60 * 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("插件 HarryMapPay v1.0.0 已成功禁用");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("mp")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以执行此命令。");
            return true;
        }

        Player player = (Player) sender;

        if (args.length >= 3 && args[0].equalsIgnoreCase("pay")) {
            try {
                int amount = Integer.parseInt(args[1]);
                if (amount <= 0) {
                    player.sendMessage(ChatColor.RED + "请输入大于0的充值金额。");
                    return true;
                }
                initiatePayment(player, amount, args.length > 3 ? args[2] : "");
                recordPaymentHistory(player, "发起支付请求", amount, args.length > 3 ? args[2] : "");
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "请输入有效的充值金额（整数）。");
                return true;
            }
        }

        return false;
    }

    private void initiatePayment(Player player, int amount, String note) {
        if (paymentTimeouts.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "你已经发起了支付请求，请完成支付或等待超时。");
            return;
        }

        player.sendMessage(ChatColor.RED + "注意!!!请注意您已经年满十八岁并且具有完全民事行为能力，可以独立进行民事活动，懂得对自己行为负责后再进行付款!!!未成年充值退全额永久ban!!!");
        player.sendMessage(ChatColor.RED + "汇率为1CNY=100点券，理性消费，树立正确消费观！！！");

        player.sendMessage("请在120s内完成支付，如果支付完成，请点击" + ChatColor.RED + "这里" + ChatColor.RESET + "以结束支付。");
        paymentTimeouts.put(player, System.currentTimeMillis() + 120000);
    }

    private void recordPaymentHistory(Player player, String action, int amount, String note) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());
        String playerUUID = player.getUniqueId().toString();
        String playerIP = player.getAddress().getAddress().getHostAddress();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentHistoryFile, true))) {
            writer.write(playerUUID + "," + playerIP + "," + timestamp + "," + action + "," + amount + "," + note + "\n");
        } catch (IOException e) {
            getLogger().severe("支付历史记录失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void backupPaymentHistory() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String backupTimestamp = dateFormat.format(new Date());
        String backupFileName = "backups_" + backupTimestamp + ".csv";
        File backupFile = new File(paymentHistoryBackupsFolder, backupFileName);

        try (FileReader reader = new FileReader(paymentHistoryFile);
             BufferedReader bufferedReader = new BufferedReader(reader);
             FileWriter writer = new FileWriter(backupFile);
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line + "\n");
            }

            getLogger().info("支付历史备份成功：" + backupFileName);
        } catch (IOException e) {
            getLogger().severe("支付历史备份失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            getLogger().severe("创建文件失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}