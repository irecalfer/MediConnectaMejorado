package com.calferinnovate.mediconnecta.View.Sesion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;



import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.View.Home.HomeActivity;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Empleado;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.Model.PeticionesJson;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SeleccionUnidadViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * Fragmento donde se realiza la selección de unidad
 */
public class SeleccionUnidadFragment extends Fragment {
    private Button botonFinalizar;
    private TextInputEditText nombre, cod_empleado, cargo;
    private AutoCompleteTextView areaSP, unidadesSP;
    private Empleado empleado;
    private ImageView foto;
    private ArrayList<Unidades> unidadesArrayList = new ArrayList<>();
    private ClaseGlobal claseGlobal;
    private Unidades unidades;
    private ArrayList<String> listaAreas = new ArrayList<>();
    private ArrayList<String> listaUnidades = new ArrayList<>();
    private ArrayAdapter<String> areasAdapter;
    private ArrayAdapter<String> unidadesAdapter;
    private SeleccionUnidadViewModel seleccionUnidadViewModel;
    private PeticionesJson peticionesJson;
    private String areaSeleccionada;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_seleccion_unidad.xml
     * Llama a inicializaVariables(vista)
     *
     * @param inflater           the inflater
     * @param container          the container
     * @param savedInstanceState the saved instance state
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vista = inflater.inflate(R.layout.fragment_seleccion_unidad, container, false);
        inicializaVariables(vista);

        inicializaViewModel();

        return vista;
    }

    /**
     * Método que Inicializa variables.
     * Obtiene instancia de clase global y sus objetos.
     * Enlaza los recursos de la UI con las variables de nuestro código.
     *
     * @param view La vista del fragmento.
     */
    public void inicializaVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        unidadesArrayList = claseGlobal.getListaUnidades();
        unidades = claseGlobal.getUnidades();
        empleado = claseGlobal.getEmpleado();

        botonFinalizar = view.findViewById(R.id.AccesoAlHome);
        nombre = view.findViewById(R.id.nombreYApellidos);
        cod_empleado = view.findViewById(R.id.cod_empleado);
        cargo = view.findViewById(R.id.cargo);
        areaSP = view.findViewById(R.id.spinnerArea);
        unidadesSP = view.findViewById(R.id.spinnerUnidad);
        foto = view.findViewById(R.id.fotoEmpleado);
    }

    /**
     * Método que configura el ViewModel SeleccionUnidadViewModel mediante la creación de un ViewModelFactory
     * que proporciona instancias de Peticiones Json y ClaseGloabl al ViewModel.
     */
    public void inicializaViewModel(){
        ViewModelArgs viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SeleccionUnidadViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        seleccionUnidadViewModel = new ViewModelProvider(requireActivity(), factory).get(SeleccionUnidadViewModel.class);
    }

    /**
     * Método llamado cuando la vista ya ha sido creada.
     * Se asigna a la variable navController el controlador de navegación correspondiente al fragmento actual.
     * Completa los datos de empleado, obtiene las áreas y pobla es spinner y establece la escucha para el botón
     * de Acceso al Home.
     *
     * @param view               Vista retornada por el inflador.
     * @param savedInstanceState Si no es nulo, este fragmento será reconstruido a partir de un
     *                              estado anterior guardado.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        completaDatosEmpleado(empleado);
        obtieneAreasyPoblaSpinner();
        listenerButtonAcceso();

    }

    /**
     * Método que setea los datos empleado en la UI.
     *
     * @param e Empleado que ha iniciado sesión.
     */
    public void completaDatosEmpleado(Empleado e) {
        nombre.setText(String.format("%s %s", e.getNombre(), e.getApellidos()));
        cargo.setText(String.valueOf(e.getNombreCargo()));
        cod_empleado.setText(String.valueOf(e.getCod_empleado()));
        //Cargamos la foto del empleado con Glide
        configuraFotoEmpleado(e);

    }


    private void configuraFotoEmpleado(Empleado e){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 300); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 300); // Tamaño máximo

        Glide.with(requireContext()).load(empleado.getFoto()).circleCrop().override(targetWidth, targetHeight).into(foto);
    }
    /**
     * Método que se llama en el onViewCreate para obtener los nombres de las areas y poblar el Spinner.
     * Llama al método obtenerDatosArea() del SeleccionUnidadViewModel y obtiene un ArrayList con
     * los nombres de las áreas.
     * Crea un adaptador con la lista de nombres de áreas obtenidas y el layout my_spinner y pobla
     * el Spinner.
     * Llama a seleccionaArea()
     */
    private void obtieneAreasyPoblaSpinner() {
        seleccionUnidadViewModel.obtenerDatosAreas().observe(getViewLifecycleOwner(), strings -> {
            listaAreas = strings;
            areasAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, listaAreas);
            areaSP.setAdapter(areasAdapter);
            seleccionaArea();
        });

    }

    /**
     * Método que obtiene que área ha sido seleccionada, limpia el ArrayList de Unidades y llama a
     * obtieneUnidadesYPoblaSpinner() según el área seleccionada.
     */
    public void seleccionaArea() {
        areaSP.setOnItemClickListener((parent, view, position, id) -> {
            listaUnidades.clear();
            unidadesArrayList.clear();
            areaSeleccionada = parent.getItemAtPosition(position).toString();
            obtieneUnidadesYPoblaSpinner();
        });
    }

    /**
     * Método encargado de obtener la lista de Unidades y poblar el Spinner.
     * Llama al método obtenerDatosUnidades de SeleccionUnidadViewModel y le pasa el área seleccionada.
     * Obtiene la lista de unidades y pobla el spinner.
     * Escucha que Unidad ha sido seleccionada y la setea en clase global.
     */
    private void obtieneUnidadesYPoblaSpinner() {
        seleccionUnidadViewModel.obtenerDatosUnidades(areaSeleccionada).observe(getViewLifecycleOwner(), string -> {
            listaUnidades = string;

            ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, listaUnidades);
            unidadesSP.setAdapter(autoCompleteAdapter);
            unidadesSP.setOnItemClickListener((parent, view, position, id) -> {
                unidades = unidadesArrayList.get(position);
                claseGlobal.setUnidades(unidades);
            });
        });

    }

    /**
     * Método que contiene el Listener del Botón de Acceso al Home.
     * Escucha al botón botónFinalizar.
     * Llama al método obtieneDatsoPacientes de SeleccionUnidadViewModel y le pasa la unidad seleccionada.
     * Obtiene los pacientes pertenecientes a la unidad e inicia la actividad HomeActivity. Si no ha
     * podido obtener los pacientes muestra un Toast de Error al obtener Datos.
     */
    public void listenerButtonAcceso(){
    botonFinalizar.setOnClickListener(v -> seleccionUnidadViewModel.obtieneDatosPacientes(unidades.getNombreUnidad()).observe(getViewLifecycleOwner(), obtenidos -> {
        if(obtenidos){
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(intent);

        }else{
            Toast.makeText(getActivity(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
        }
    }));

}




}
