# Shattered Pixel Dungeon 3.0.2 - 项目概览

## 项目信息

**项目名称**: Shattered Pixel Dungeon  
**版本**: 3.0.2  
**构建系统**: Gradle  
**编程语言**: Java  
**游戏引擎**: LibGDX  
**许可证**: GPL v3  

**官网**: https://shatteredpixel.com/  
**GitHub**: https://github.com/00-Evan/shattered-pixel-dungeon  

---

## 项目结构

```
shattered-pixel-dungeon-3.0.2/
├── core/                        # 核心游戏代码
│   ├── src/main/java/          # Java 源代码
│   │   └── com/shatteredpixel/shatteredpixeldungeon/
│   │       ├── actors/          # 游戏角色系统
│   │       │   ├── Actor.java
│   │       │   ├── Char.java    # 角色基类
│   │       │   ├── hero/        # 英雄相关
│   │       │   ├── mobs/        # 怪物相关
│   │       │   └── buffs/       # 效果系统
│   │       ├── items/           # 物品系统
│   │       ├── levels/          # 关卡系统
│   │       ├── mechanics/       # 游戏机制
│   │       ├── scenes/          # 游戏场景
│   │       │   ├── GameScene.java
│   │       │   ├── PixelScene.java
│   │       │   ├── PlayerEventLogger.java  # [新增]
│   │       │   └── ...
│   │       ├── ui/              # UI 组件
│   │       ├── windows/         # UI 窗口
│   │       ├── utils/           # 工具类
│   │       │   ├── ScreenshotSaver.java    # [新增]
│   │       │   ├── PerformanceSampler.java # [新增]
│   │       │   └── ...
│   │       ├── tiles/           # 地砖系统
│   │       ├── effects/         # 视觉效果
│   │       ├── sprites/         # 精灵处理
│   │       ├── Dungeon.java     # 地牢管理
│   │       ├── ShatteredPixelDungeon.java  # 主入口
│   │       └── ...
│   └── src/main/assets/         # 游戏资源
│       ├── sprites/             # 精灵图 (1000+ PNG)
│       ├── sounds/              # 音效 (90+ MP3)
│       ├── splashes/            # 启动画面 (12 JPG)
│       ├── environment/         # 环境图
│       ├── fonts/               # 字体
│       └── gdx/                 # GDX 框架资源
│
├── desktop/                     # 桌面版本 (Windows/Mac/Linux)
├── android/                     # Android 版本
├── ios/                         # iOS 版本
├── SPD-classes/                 # 基础类库
│   └── com/watabou/            # Watabou 游戏引擎
│       ├── noosa/              # 图形和声音
│       ├── input/              # 输入处理
│       ├── glwrap/             # OpenGL 包装
│       ├── gltextures/         # 纹理管理
│       └── utils/              # 工具类
│
├── services/                    # 服务模块
│   ├── updates/
│   │   └── githubUpdates/      # GitHub 更新检查
│   └── news/                    # 新闻系统
│
├── build.gradle                 # 主构建配置
├── gradle.properties            # Gradle 属性
├── settings.gradle              # 模块设置
├── gradlew / gradlew.bat        # Gradle 包装器
└── ...
```

---

## 最近合并 (dev-Sissie → dev_ltz)

### 合并信息
- **合并提交 ID**: c6d0210
- **合并时间**: 2026-05-18 00:07:55
- **合并状态**: ✅ 完成，无冲突
- **工作目录**: Clean (无未提交更改)

### 修改摘要

#### 新增功能

1. **截图保存功能** (`ScreenshotSaver.java`)
   - 快捷键: `Shift+P`
   - 保存位置: `~/Desktop/ShatteredPD-Screenshots/`
   - 文件名格式: `SPD_yyyyMMdd_HHmmss.png`
   - 自动翻转图像 (OpenGL 帧缓冲区是倒立的)

2. **玩家事件日志** (`PlayerEventLogger.java`)
   - 记录位置: `logs/player-events.log`
   - 记录内容:
     - 药水使用事件 (PotionOfHealing)
     - 物品动作
     - 楼层变化 (InterlevelScene)
   - 格式: `timestamp | level | source | event | details`

3. **性能采样工具** (`PerformanceSampler.java`)
   - 采样间隔: 2 秒
   - 记录指标:
     - 平均 FPS 和 GDX FPS
     - 当前深度 (Dungeon depth)
     - 活动怪物数量
     - 视野内怪物数量
     - 负载等级 (empty/normal/heavy)
   - 用途: ISO 25010 "性能效率" 分析

#### 修改的文件

- `build.gradle` - 构建配置更新
- `gradle.properties` - Gradle 属性更新
- `gradlew` - 包装器脚本更新
- `Assets.java` - 资源管理修改
- `SPDAction.java` - 快捷键配置
- `Hero.java` - 英雄类增强
- `Item.java` - 物品系统修改
- `PotionOfHealing.java` - 治疗药水日志集成
- `GameScene.java` - 游戏场景修改
- `InterlevelScene.java` - 关卡转换修改
- `PixelScene.java` - 像素场景修改 (截图支持)

#### 删除的文件

- `.vscode/settings.json` - VS Code 配置删除
- `test.md` - 测试文档删除

---

## 主要游戏系统

### 1. 角色系统 (Actors)
- **Hero** - 玩家操控角色
- **Mobs** - 各类怪物 (敌对 NPC)
- **Char** - 所有角色的基类
- **Buffs** - 状态效果系统

### 2. 物品系统 (Items)
- 武器、护甲、装备
- 药水、卷轴、戒指
- 炸弹、种子、工具
- 背包管理

### 3. 关卡系统 (Levels)
- 5 个主要地牢区域
  - 下水道 (Sewers)
  - 监狱 (Prison)
  - 洞穴 (Caves)
  - 城市 (City)
  - 大厅 (Halls)
- 房间生成系统
- 地砖映射系统

### 4. UI 系统
- 状态面板 (StatusPane)
- 工具栏 (Toolbar)
- 物品栏 (InventoryPane)
- 窗口系统 (40+ 窗口类)
- 按钮和交互元素

### 5. 场景系统
- **TitleScene** - 标题场景
- **GameScene** - 游戏主场景
- **InterlevelScene** - 关卡转换
- **PixelScene** - 基础像素渲染场景

---

## 构建配置

### Gradle 模块
```gradle
root/
├── core (主游戏)
├── desktop (桌面应用)
├── android (Android 应用)
├── ios (iOS 应用)
├── SPD-classes (基础库)
└── services (服务模块)
```

### 平台支持
- **Windows** (Desktop) ✅
- **macOS** (Desktop) ✅
- **Linux** (Desktop) ✅
- **Android** 5.0+ ✅
- **iOS** 13.0+ ✅

---

## 资源统计

| 资源类型 | 数量 | 位置 |
|---------|------|------|
| 精灵图 | 1000+ | `assets/sprites/` |
| 音效 | 90+ | `assets/sounds/` |
| 启动画面 | 12 | `assets/splashes/` |
| 环境图 | 多个 | `assets/environment/` |
| 字体 | 1 | `assets/fonts/` |
| **总文件数** | **1805+** | |

---

## 核心类

| 类名 | 位置 | 功能 |
|------|------|------|
| `ShatteredPixelDungeon` | - | 游戏主入口 |
| `Dungeon` | - | 地牢管理器 |
| `GameScene` | scenes/ | 游戏主场景 |
| `Hero` | actors/hero/ | 玩家角色 |
| `Mob` | actors/mobs/ | 怪物基类 |
| `Item` | items/ | 物品基类 |
| `Level` | levels/ | 关卡基类 |
| `PlayerEventLogger` | utils/ | [新] 事件日志 |
| `PerformanceSampler` | utils/ | [新] 性能采样 |
| `ScreenshotSaver` | utils/ | [新] 截图保存 |

---

## 快捷键

| 快捷键 | 功能 |
|--------|------|
| **Shift+P** | 保存截图到 Desktop |
| Alt+Enter | 全屏切换 |

---

## 最近 Git 历史

```
c6d0210 - Merge remote-tracking branch 'remotes/origin/dev-Sissie' into dev_ltz
143a9e7 - revert
b5dade3 - Revert "Merge remote-tracking branch 'remotes/origin/dev-Sissie' into dev_ltz"
7cb7c46 - Solve the blank image generated after reversing the picture
eb97862 - Turnover Screenshot
b3b81fb - Merge remote-tracking branch 'remotes/origin/dev-Sissie' into dev_ltz
15849a8 - start_version
137645e - onGameFrame
f2ac808 - PerformanceSampler
```

---

## 重要文件

| 文件 | 描述 |
|------|------|
| [README.md](README.md) | 项目说明 |
| [build.gradle](build.gradle) | 主构建配置 |
| [core/build.gradle](core/build.gradle) | 核心模块配置 |
| [docs/getting-started-desktop.md](docs/getting-started-desktop.md) | 桌面开发指南 |
| [docs/getting-started-android.md](docs/getting-started-android.md) | Android 开发指南 |

---

## 开发信息

### 编码标准
- 使用 Java 命名约定 (CamelCase)
- GPL v3 许可证头注释
- 包结构: `com.shatteredpixel.shatteredpixeldungeon.*`

### 版本兼容性
- **v1.2.3** - 存档格式参考 (旧版本)
- **v2.3.2** - 最早支持的存档版本
- **v3.0.0** - 当前主要版本 (v831)

### 平台支持类
- `PlatformSupport` - 平台适配接口
- `DeviceCompat` - 设备兼容性工具
- `FileUtils` - 文件操作工具

---

## 注意事项

1. ⚠️ **此仓库不接受 Pull Requests**
   - 代码仅供参考，不允许社区贡献
   - 可以提交 Issue 报告 (Bug、特性请求等)

2. 📱 **Google Play 分发**
   - 在 `docs/getting-started-android.md` 末尾有分发指南

3. 🎮 **游戏发布渠道**
   - Google Play Store
   - App Store (iOS)
   - Steam
   - GOG.com
   - Itch.io
   - GitHub Releases

4. 💰 **支持开发者**
   - 在 Patreon 上支持原作者: https://www.patreon.com/ShatteredPixel
   - 官方博客: https://www.shatteredpixel.com/blog/

---

**生成时间**: 2026-05-18  
**生成者**: Project Analysis Tool  
**状态**: ✅ 项目就绪，所有文件正常
