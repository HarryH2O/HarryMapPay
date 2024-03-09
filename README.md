# HarryMapPay 插件 Wiki

## 概述

**HarryMapPay** 插件是一个为 Minecraft 服务器设计的付款插件，旨在提供简便的支付系统和详细的支付历史记录。通过该插件，玩家可以进行虚拟货币的充值，而服务器管理员可以轻松管理支付历史。

## 安装

1. **下载插件：** 从 [https://github.com/HarryH2O/HarryMapPay/releases/tag/v1.0] 下载最新版本的 HarryMapPay 插件。

2. **复制到插件文件夹：** 将下载的 JAR 文件复制到你的 Minecraft 服务器的插件文件夹。

3. **重启服务器：** 在服务器控制台中输入指令 `/reload` 或重启服务器，以加载新的插件。

## 功能特点

### 1. 二维码支付

- 通过 `/mp pay` 指令发起支付请求，生成二维码。
- 玩家扫描二维码即可完成支付。

### 2. 支付历史记录

- 自动记录每笔支付的详细信息，包括玩家、金额、时间等。
- 可通过配置文件指定支付历史记录的存储路径。

### 3. 支付超时提醒

- 设置支付超时时间，确保支付在规定时间内完成。
- 超时后自动提醒玩家重新发起支付请求。

### 4. 多语言支持

- 插件支持多语言，可通过配置文件切换语言。

## 使用指南

### 1. 发起支付请求

- 使用 `/mp pay <金额>` 发起支付请求。
- 确保金额大于 0，并可选择添加备注。

### 2. 查看支付历史记录

- 查看详细的支付历史记录，包括时间、玩家、金额等信息。
- 可在配置文件中指定历史记录文件路径。

### 3. 配置文件

- 通过编辑配置文件，定制化插件的各项参数，如支付超时时间、备份频率等。

## 配置文件示例

```yaml
# HarryMapPay 配置文件

qr_code:
  default_url: "https://example.com"
  image_size:
    width: 128
    height: 128

payment_history:
  enabled: true
  file_path: "plugins/HarryMapPay/paymenthistory.csv"
  backup_folder_path: "plugins/HarryMapPay/paymenthistory_backups"
  backup_interval: 30

payment_timeout:
  enabled: true
  timeout_duration: 120

messages:
  initiate_payment:
    - "请注意年满十八岁并具有完全民事行为能力后再进行付款！未成年充值将永久封禁！"
    - "汇率为1CNY=100点券，理性消费，树立正确消费观！"
    - "请在120s内完成支付，如已支付，请点击 %payment_completion% 结束支付。"
  timeout:
    - "支付超时，请重新发起支付请求。"

variables:
  payment_completion: "&c这里"

language: "zh_CN"
```

## 常见问题

1. **支付超时如何处理？**
   - 若支付超时，请重新发起支付请求。

2. **如何备份支付历史记录？**
   - 插件会自动备份，可在配置文件中设置备份频率。

3. **支付金额是否有限制？**
   - 金额需大于 0，且只能为整数。

4. **如何切换语言？**
   - 在配置文件中修改 `language` 参数。

## 反馈与支持

如果你在使用过程中遇到问题或有建议，欢迎在 [https://github.com/HarryH2O/HarryMapPay] 中提出 issue 或联系开发者。

感谢你选择使用 HarryMapPay 插件，祝你的服务器管理愉快！
