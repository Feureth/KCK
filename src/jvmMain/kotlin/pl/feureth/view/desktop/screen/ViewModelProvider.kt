package pl.feureth.view.desktop.screen

object ViewModelProvider {

    private val viewModels = HashMap<String, ViewModel>()

    fun <T: ViewModel> provide(clazz: Class<T>): T {
        val key = clazz.canonicalName
        if (viewModels.contains(key).not()) {
            viewModels[key] = clazz.getDeclaredConstructor().newInstance()
        }
        return viewModels[key] as T
    }
}