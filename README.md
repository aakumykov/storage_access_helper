Класс предназначен для запроса доступа на чтение, запись, чтение+запись для версий Android младше 11 (api 30) и полного доступа к хранилищу (MANAGE_EXTERNAL_STORAGE) для версий 11 и выше.

### Подключение

Через Gradle из репозитория [JitPack](https://jitpack.io/#aakumykov/storage_access_helper):
```
    implementation 'com.github.aakumykov:storage_access_helper:1.0.1-alpha'
```

### Использование:

1) Создаём экземпляр StorageAccessHelper для Fragment или Activity:
```
  StorageAccessHelper.create(fragment)
  StorageAccessHelper.create(activity)
```
2) Подготавливаем к получению событий запроса доступа:
```
  storageAccessHelper.prepareForReadAccess()
  storageAccessHelper.prepareForWriteAccess()
  storageAccessHelper.prepareForFullAccess()
```
То же в Kotlin-стиле:
```
  storageAccessHelper = StorageAccessHelper.create(this).apply {
      prepareForReadAccess()
      prepareForWriteAccess()
      prepareForFullAccess()
  }
```
3) Вызываем метод с предварительным запросом доступа к хранилищу:
```
  storageAccessHelper.requestReadAccess { readFile() }
  storageAccessHelper.requestWriteAccess { writeFile() }
  storageAccessHelper.requestFullAccess { doAnything() }
```

### Дополнительно
Открытие системного диалога настроек хранилища:
```
  StorageAccessHelper.openStorageAccessSettings(fragment)
  StorageAccessHelper.openStorageAccessSettings(activity)
```
