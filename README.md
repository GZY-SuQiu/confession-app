# 心动表白（ConfessionApp）

单 Activity 纯 Java 的 Android 表白神器，零第三方依赖，APK 很小。

## 功能
1. **设置页**：输入对方名字 + 自定义表白文案（可留空用默认经典款）
2. **表白页**：跳动的心 + 大字文案，两个按钮：
   - 「愿意 ♥」→ 心形爆开 + 成功结算页 + 震动
   - 「不愿意」→ 按钮会逃跑！文案逐句升级（你确定？→ 再想想？→ …→ 它跑不动了），4 次后「不愿意」残废变灰，只能点愿意
3. 竖屏、全屏渐变背景

## 编译
本机（Minis 沙箱）无法编译 APK（JVM 跑不起来），出包走 GitHub Actions：

```
push 到 GitHub → Actions 自动跑 assembleRelease → 下载 artifact 里的 APK
```

或在任意有 Android Studio / gradle 的电脑上：`gradle assembleRelease`

## 安装
APK 直接传到手机 → 文件管理器打开 → 允许「安装未知应用」即可安装（未签名证书自签，首次安装需点「仍要安装」）。
