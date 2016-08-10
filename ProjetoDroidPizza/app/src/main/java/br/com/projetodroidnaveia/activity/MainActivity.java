package br.com.projetodroidnaveia.activity;

import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "br.com.projetodroidnaveia.MESSAGE";
    //mecanismo para a voz
    private TextToSpeech objTextoParaVoz;

    private Button btnIniciarAtendimento;
    private static final int PARAM_REQUISICAO_PARA_TEXTO_FALADO_PIZZA = 1;
    private static final int PARAM_REQUISICAO_PARA_TEXTO_FALADO_BEBIDA = 2;
    private static final int PARAM_REQUISICAO_PARA_TEXTO_FALADO_CONFIRM_PEDIDO = 3;

    private Double valorPedido = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIniciarAtendimento = (Button) findViewById(R.id.btnIniciar);

        objTextoParaVoz = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                objTextoParaVoz.setLanguage(new Locale("pt"));

                objTextoParaVoz.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    //iniciando uma fala
                    @Override
                    public void onStart(String utteranceId) {

                    }

                    //acabou de falar
                    @Override
                    //chamado qdo o usuario terminar de falar
                    public void onDone(String utteranceId) {
                        iniciarCapturaFala(Integer.parseInt(utteranceId));

                    }

                    //iniciando ao recuperar fala
                    @Override
                    public void onError(String utteranceId) {

                    }
                });
            }
        });

        btnIniciarAtendimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int speak = objTextoParaVoz.speak("Bem vindo ao Izidro Vialle Pizza. Escolha o sabor da sua pizza.", TextToSpeech.QUEUE_FLUSH, null,
                        String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_PIZZA));
            }
        });
    }

    private void iniciarCapturaFala(Integer flagReconhecimento) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora");
        startActivityForResult(intent, flagReconhecimento);
    }

    public void anotherActivity(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "ANOTHER ACTIVITY";//editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent){
        ArrayList<String> listaPossivelFalas = dataIntent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        //verifica se a lista esta preenchida
        if(listaPossivelFalas != null && listaPossivelFalas.size() > 0){
            boolean achou = false;
            for(int i = 0; i < listaPossivelFalas.size(); i++)
            {
                String resposta = listaPossivelFalas.get(i);
                if(requestCode == PARAM_REQUISICAO_PARA_TEXTO_FALADO_PIZZA){
                    if(resposta.equalsIgnoreCase("Presunto") || resposta.equalsIgnoreCase("Pizza Presunto") ||
                            resposta.equalsIgnoreCase("Pizza de Presunto")){
                        achou = true;
                        valorPedido = valorPedido + 28.00;
                        objTextoParaVoz.speak(" Entendi. Pizza de presunto. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null,
                                String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_BEBIDA));
                    }

                    if(resposta.equalsIgnoreCase("Portuguesa") || resposta.equalsIgnoreCase("Pizza Portuguesa") ||
                            resposta.equalsIgnoreCase("Pizza de Portuguesa")){
                        achou = true;
                        valorPedido = valorPedido + 32.00;
                        objTextoParaVoz.speak(" Entendi. Pizza portuguesa. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null,
                                String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_BEBIDA));
                    }

                    if(resposta.equalsIgnoreCase("Camarão") || resposta.equalsIgnoreCase("Pizza Camarão") ||
                            resposta.equalsIgnoreCase("Pizza de Camarão")){
                        achou = true;
                        valorPedido = valorPedido + 40.00;
                        objTextoParaVoz.speak(" Entendi. Pizza de camarão. Escolha sua bebida.", TextToSpeech.QUEUE_FLUSH, null,
                                String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_BEBIDA));
                    }

                }else if(requestCode == PARAM_REQUISICAO_PARA_TEXTO_FALADO_BEBIDA){
                    if(resposta.equalsIgnoreCase("coca") || resposta.equalsIgnoreCase("coca cola") ||
                        resposta.equalsIgnoreCase("coquinha")){
                        achou = true;
                        valorPedido = valorPedido + 7.00;
                        objTextoParaVoz.speak("Entendi. Coca cola. O valor do seu pedido é " + String.valueOf(valorPedido) + ". Confirma o seu pedido?", TextToSpeech.QUEUE_FLUSH, null,
                                String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_CONFIRM_PEDIDO));
                    }

                    if(resposta.equalsIgnoreCase("guarana") || resposta.equalsIgnoreCase("guará") ||
                            resposta.equalsIgnoreCase("guaraná")){
                        achou = true;
                        valorPedido = valorPedido + 5.00;
                        objTextoParaVoz.speak("Entendi. Guaraná. O valor do seu pedido é " + String.valueOf(valorPedido) + ". Confirma o seu pedido?", TextToSpeech.QUEUE_FLUSH, null,
                                String.valueOf(PARAM_REQUISICAO_PARA_TEXTO_FALADO_CONFIRM_PEDIDO));
                    }
                }else if(requestCode == PARAM_REQUISICAO_PARA_TEXTO_FALADO_CONFIRM_PEDIDO){
                    if(resposta.equalsIgnoreCase("sim") || resposta.equalsIgnoreCase("ok") ||
                            resposta.equalsIgnoreCase("yes") || resposta.equalsIgnoreCase("confirmo") ||
                            resposta.equalsIgnoreCase("confirmado")){
                        achou = true;
                        objTextoParaVoz.speak("Ok. Pedido confirmado. Em 50 minutos seu pedido será entregue em sua residência. " +
                                "Droid pizza agradece. Obrigado pela preferência.", TextToSpeech.QUEUE_FLUSH, null, null);
                    }

                    if(resposta.equalsIgnoreCase("não") || resposta.equalsIgnoreCase("not") ){
                        achou = true;
                        objTextoParaVoz.speak("Ok pedido cancelado. Faça seu pedido novamente.", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                    valorPedido = 0.0;
                }

                if(!achou){
                    Toast.makeText(this, "Não entendi.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
