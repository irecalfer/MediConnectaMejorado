package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Area;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.google.android.material.tabs.TabLayout;

/**
 * Fragmento que proporciona un TabLayout para desplazarse por los datos del paciente y un fragment container
 * donde se reemplazará los fragmentos de detalle del paciente.
 */
public class DetallePacientesFragment extends Fragment implements IOnBackPressed {

    private TabLayout tabLayoutPaciente;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private ClaseGlobal claseGlobal;
    private String nombreArea;


    /**
     * Método llamado cuando se crea la vista del fragmento.
     * Infla el diseño de la UI desde el archivo XML fragment_detalle_pacientes.xml.
     * Establece el título del Fragmento y configura el ViewModel.
     *
     * @param inflater inflador utilizado para inflar el diseño de la UI.
     * @param container Contenedor que contiene la vista del fragmento
     * @param savedInstanceState Estado de guardado de la instancia del fragmento
     *
     * @return vista Es la vista inflada del fragmento.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pacientes, container, false);
        asignarValoresAVariables(view);
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        getActivity().setTitle("Detalles Paciente");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listenerTabLayoutDetalle();
    }




    /**
     * Método que inicializa las variables miembro y enlaza con los recursos de la UI.
     * @param view La vista inflada.
     */
    public void asignarValoresAVariables(View view) {
        claseGlobal = ClaseGlobal.getInstance();
        tabLayoutPaciente = view.findViewById(R.id.tabLayoutDetallePacientes);

    }



    /**
     * Método que implementa la escucha de los Tabs del TabLayout, por defecto muestra GeneralPacientesFragment
     * si un tab es seleccionado navega hasta dicho fragmento.
     */
    public void listenerTabLayoutDetalle() {
        if (tabLayoutPaciente.getTabCount() > 0) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
        }

        tabLayoutPaciente.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabSeleccionado = (String) tab.getText();

                assert tabSeleccionado != null;
                navegaAlFragmento(tabSeleccionado);
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    /**
     * Método que navega al fragmento según el Tab seleccionado. En caso de ser seleccionado el Tab Pautas
     * llama a seleccionarTipoPautasArea().
     * @param tabSeleccionado
     */
    public void navegaAlFragmento(String tabSeleccionado){
        switch (tabSeleccionado) {
            case "General":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new GeneralPacientesFragment()).commit();
                break;
            case "Contactos":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ContactoFamiliaresPacienteFragment()).commit();
                break;
            case "Clínica":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ClinicaPacientesFragment()).commit();
                break;
            case "Pautas":
                seleccionarTipoPautasAreas();
                break;
            case "Parte":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PartePacientesFragment()).commit();
                break;
            case "Parte Caídas":
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new ParteCaidasPacientesFragment()).commit();
                break;
        }
    }

    /**
     * Método que obtiene el Paciente seleccionado y verifica si pertenece al area de Geriatría o Salud Mental,
     * según cual sea navega a un fragmento de Pautas diferente.
     */
    public void seleccionarTipoPautasAreas() {
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), pacientes -> {
            for (Unidades unidades : claseGlobal.getListaUnidades()) {
                if (unidades.getId_unidad() == pacientes.getFkIdUnidad()) {
                    for (Area areas : claseGlobal.getListaAreas()) {
                        if (unidades.getFk_area() == areas.getId_area()) {
                            nombreArea = areas.getNombre();
                        }
                    }
                }
            }
        });
        if (nombreArea.equals("Geriatría")) {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasPacientesGeriatriaFragment()).commit();
        } else {
            getParentFragmentManager().beginTransaction().replace(R.id.fragmentContainerDetallePacientes, new PautasSaludMentalPacientesFragment()).commit();
        }
    }

    /**
     * Método que agrega la lógica específica del fragmento para manejar el restroceso.
     * Al presionar back volvería al PacientesFragment.
     * @return true si el fragmento manejar el retroceso, false en caso contrario.
     */
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }


}

