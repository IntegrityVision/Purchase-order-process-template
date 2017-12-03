var camTasklistConf = {
    // change the app name and vendor
    app: {
      name: 'BPM',
      vendor: 'Integrity Vision'
    },
    //
    // configure the date format
    // "dateFormat": {
    //   "normal": "LLL",
    //   "long":   "LLLL"
    // },
    //
    "locales": {
       "availableLocales": ["uk"],
       "fallbackLocale": "uk"
     },
    //
    customScripts: {
        ngDeps: ['ngMask'],
        deps: ['ngMask'],
        //mistake in official documentation
        paths: {
            'ngMask': 'scripts/ngMask.min'
        }
    },

    "shortcuts": {
        "claimTask": {
            "key": "ctrl+alt+c",
            "description": "claims the currently selected task"
        },
        "focusFilter": {
            "key": "ctrl+shift+f",
            "description": "set the focus to the first filter in the list"
        },
        "focusList": {
            "key": "ctrl+alt+l",
            "description": "sets the focus to the first task in the list"
        },
        "focusForm": {
            "key": "ctrl+alt+f",
            "description": "sets the focus to the first input field in a task form"
        },
        "startProcess": {
            "key": "ctrl+alt+p",
            "description": "opens the start process modal dialog"
        }
    }
};
