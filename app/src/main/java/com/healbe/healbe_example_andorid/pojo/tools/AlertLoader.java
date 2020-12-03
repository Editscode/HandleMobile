package com.healbe.healbe_example_andorid.pojo.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.healbe.healbe_example_andorid.R;

public class AlertLoader {
    private AlertDialog loadDialog;
    private Handler handler;
    private Context context;

    public final static int START = 0;
    public final static int UNCORRECT_LOGIN = 1;
    public final static int BAD_REQUEST = 2;
    public final static int STOP_LOGIN = 3;
    public final static int WTF = 4;
    public final static int UNKNOW = 5;

    public final static int STOP_REG = 6;
    public final static int CRASH_REG = 7;

    public final static int END_JSON = 8;
    public final static int END_SUBMIT = 9;
    public final static int CRASH_SUBMIT = 10;


    //Конструктор принимает строку, которая будет рядом с лоадером
    public AlertLoader(Context contextParent, String message) {
        this.context = contextParent;

        //Установка сообщения
        View content = LayoutInflater.from(context).inflate(R.layout.dialog_load, null);
        TextView textView = (TextView) content.findViewById(R.id.text_alert);
        textView.setText(message);


        //создание Диалогового окна
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setCancelable(false)
                .setView(content);
        loadDialog = builder.create();


        //Создание Handler
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == START) {
                    loadDialog.show();
                    return;
                }

                loadDialog.cancel();
                String str = "";
                switch (msg.what) {
                    case UNCORRECT_LOGIN: //
                        str = "Неверный еmail или пароль";
                        break;
                    case BAD_REQUEST: //400
                        str = "Неверный запрос";
                        break;
                    case STOP_LOGIN: //200
                        str = "Вы успешно вошли!";
                        break;
                    case STOP_REG:
                        str = "Регистрация прошла успешна!";
                        break;
                    case CRASH_REG:
                        str = "Регистрация не удалась!";
                        break;
                    case WTF: //???
                        str = "Что-то пошло не так...";
                        break;
                    case UNKNOW: //???
                        str = "Пришел неизвестный ответ";
                        break;
                    case END_JSON: //???
                        str = "Сбор данных завершен!";
                        break;
                    case END_SUBMIT: //???
                        str = "Новые данные отправлены!";
                        break;
                    case CRASH_SUBMIT: //???
                        str = "Ошибка при отправке данных!";
                        break;
                }

                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void start() {
        handler.sendEmptyMessage(START);
    }

    public void stop(int code) {
        handler.sendEmptyMessage(code);
    }
}

