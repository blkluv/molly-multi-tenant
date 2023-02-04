/** 布局配置 */
interface ILayoutSettings {
  /** 是否显示 Settings Panel */
  showSettings: boolean
  /** 是否显示标签栏 */
  showTagsView: boolean
  /** 是否显示侧边栏 Logo */
  showSidebarLogo: boolean
  /** 是否固定 Header */
  fixedHeader: boolean
  /** 是否显示消息通知 */
  showNotify: boolean
  /** 是否显示切换主题按钮 */
  showThemeSwitch: boolean
  /** 是否显示全屏按钮 */
  showScreenfull: boolean
  /** 是否显示灰色模式 */
  showGreyMode: boolean
  /** 是否显示色弱模式 */
  showColorWeakness: boolean
  /** 是否显示搜索租户 */
  showSearchTenant: boolean
  /** 是否显示搜索路由 */
  showSearchRoute: boolean
  /** 是否显示控件尺寸 */
  showControlSize: boolean
  /** 控件尺寸 */
  controlSize: string
}

const layoutSettings: ILayoutSettings = {
  showSettings: true,
  showTagsView: true,
  fixedHeader: true,
  showSidebarLogo: true,
  showNotify: true,
  showThemeSwitch: true,
  showScreenfull: true,
  showGreyMode: false,
  showColorWeakness: false,
  showSearchTenant: true,
  showSearchRoute: true,
  showControlSize: true,
  controlSize: "default"
}

export default layoutSettings