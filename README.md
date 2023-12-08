# android-hybrid
面向前端简单讲解 hybrid 原理

- 使用 shouldOverrideUrlLoading() 方法回调拦截 url，需要指定协议的约束
- 使用 onJsAlert、onJsConfirm、onJsPrompt 等方法拦截 JS 对话框 alert()、confirm()、prompt() 消息对话框消息。其中WebView上alert无效，需要定制WebChromeClient处理弹出。
- 使用 addJavascriptInterface() 方法：Android 中的 WebView 提供了 addJavascriptInterface() 方法，该方法允许将 Java 对象暴露给 JavaScript 代码。通过这种方式，JavaScript 代码可以直接调用 Java 对象的方法。
- 使用 evaluateJavascript() 方法：Android 中的 WebView 还提供了 evaluateJavascript() 方法，该方法可以执行 JavaScript 代码，并获取返回值。（Android 4.4以上）
- 使用 loadUrl() 方法：与 evaluateJavascript() 方法类似，但没有返回值，可能会引起页面刷新，没有版本限制。