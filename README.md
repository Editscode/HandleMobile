# Healbe SDK Example
Healbe SDK quickstart. Shows login, connection and data using process.

## Dependencies
To use in your project add to your `build.gradle`:
 
```groovy
repositories { 
    maven { url  "https://dl.bintray.com/healbe/healbesdk" }
}
```

And add SDK dependency:

```groovy
implementation("com.healbe:healbesdk:1.0.4") { transitive = true }
```

## Documentation

You can read or download javadoc [here](https://healbesdk.bitbucket.io/android/)

## Quickstart
This example located in [SimpleActivity](https://bitbucket.org/Healbe/healbe-public-android-sdk/src/master/app/src/main/java/com/healbe/healbe_example_andorid/SimpleActivity.java) class. 
You can make it "Launch Activity" in manifest and run instead of more complex example which is running by default.

```java
public class SimpleActivity extends AppCompatActivity {
    private CompositeDisposable unsubscribeOnDestroy = new CompositeDisposable();
    private TextView state;
    private TextView pulse;
    private ProgressBar progress;
    private String userEmail = "user@healbe.com"; // your healbe user email
    private String userPassword = "password"; // your healbe user password
    private HealbeDevice device = new HealbeDevice("deviceName", "00:00:00:00:00:00", "000000"); // your device name, mac-address and pin
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);

        pulse = findViewById(R.id.text);
        progress = findViewById(R.id.progress);
        state = findViewById(R.id.state);

        progress.setVisibility(View.VISIBLE);
        state.setVisibility(View.VISIBLE);
        pulse.setVisibility(View.GONE);

        //check permissions, turn on bluetooth and run
        Permissions.checkBluetoothPermission(this::runChain, this);
    }

    private void runChain() {
        unsubscribeOnDestroy.add(initSdk()
                .andThen(prepareSession())
                .andThen(login())
                .andThen(setupDevice())
                .andThen(connect())
                .andThen(observeHeartRate())
                .subscribe(this::showPulse, this::showError));
    }

    private Completable initSdk() {
        return Completable.defer(() -> HealbeSdk.init(getApplicationContext()));
    }

    private Completable prepareSession() {
        return Completable.defer(() -> HealbeSdk.get().USER.prepareSession().ignoreElement());
    }

    private Completable login() {
        return Completable.defer(() -> HealbeSdk.get().USER.login(userEmail, userPassword)
                .flatMapCompletable(this::checkState));
    }

    private Completable setupDevice() {
        return Completable.defer(() -> HealbeSdk.get().GOBE.set(device));
    }

    private Completable connect() {
        return Completable.defer(() -> HealbeSdk.get().GOBE.connect()
                // observe connection states
                .andThen(HealbeSdk.get().GOBE.observeConnectionState())
                // show state
                .doOnNext(clientState ->
                        state.post(() -> state.setText(String.valueOf(clientState))))
                // wait for READY state
                .filter(clientState -> clientState == ClientState.READY)
                // now wristband is READY
                .firstElement()
                // we can ignore state because of filter
                .ignoreElement()
        );
    }

    private Observable<HeartRate> observeHeartRate() {
        return Observable.defer(() -> HealbeSdk.get().TASKS.observeHeartRate()
                // observe on main thread for correctly update ui
                .observeOn(AndroidSchedulers.mainThread()));
    }

    @SuppressLint("SetTextI18n")
    private void showPulse(HeartRate heartRate) {
        progress.setVisibility(View.GONE);

        if (!heartRate.isEmpty()) {
            state.setVisibility(View.GONE);
            pulse.setVisibility(View.VISIBLE);
            pulse.setText("Pulse: " + heartRate.getHeartRate() + " bpm");
        } else {
            state.setVisibility(View.VISIBLE);
            pulse.setVisibility(View.GONE);
            state.setText("Pulse not found, did you wear your GoBe?");
        }
    }

    @SuppressLint("SetTextI18n")
    private void showError(Throwable e) {
        progress.setVisibility(View.GONE);
        pulse.setVisibility(View.GONE);
        state.setText("Error occupied: " + e.getMessage());
    }

    private Completable checkState(HealbeSessionState sessionState) {
        if (HealbeSessionState.isUserValid(sessionState))
            return Completable.complete();
        else
            return Completable.error(new RuntimeException("user invalid"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // clear all subscribers
        unsubscribeOnDestroy.clear();
    }
}
```

Layout for activity:
```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SplashActivity">

    <TextView
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="32sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:gravity="center"
        tools:text="Pulse: 75 bpm" />

    <ProgressBar
        android:id="@+id/progress"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        android:layout_marginEnd="8dp"
        android:layout_marginBottom="8dp"
        android:indeterminate="true"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <TextView
        android:id="@+id/state"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:layout_marginTop="16dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/progress"
        android:gravity="center"
        tools:text="DISCONNECTED" />

</androidx.constraintlayout.widget.ConstraintLayout>
```


To see log you need place timber log tree in plant: 

```java
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
```

Also you can run and study base complex example which implements simple dashboard application for Healbe SDK current values with login and connection wizards.  