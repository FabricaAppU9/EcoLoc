package br.com.fabappu9.ecoloc.network;


import br.com.fabappu9.ecoloc.Model.Resposta;
import br.com.fabappu9.ecoloc.Model.RespostaLogin;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;



public class APIClient {

    private static RestAdapter REST_ADAPTER;

    public APIClient(){
        createAdapterIfNeeded();
    }

    private static void createAdapterIfNeeded() {
        if(REST_ADAPTER == null){
            REST_ADAPTER = new RestAdapter
                    .Builder()
                    .setEndpoint("http://devjan.esy.es/ws_app/v1")
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient())
                    .build();
        }
    }

    public RestServices getRestService(){
        return REST_ADAPTER.create(RestServices.class);
    }

    public interface RestServices{

        @GET("/usuarioDTO.php")
        void setUsuarioDTO(
                @Query("CHAVE") String chave,
                @Query("CHAMADA") String chamada,
                @Query("NOME") String nome,
                @Query("LOGIN") String login,
                @Query("SENHA") String senha,
                Callback<Resposta> callbackResposta
        );
       @GET("/usuarioDTO.php")
                void setUsuarioLoginDTO(
                @Query("CHAVE") String chave,
                @Query("CHAMADA") String chamada,
                @Query("LOGIN") String login,
                @Query("SENHA") String senha,
                Callback<RespostaLogin> callbackResposta
        );
    }
}