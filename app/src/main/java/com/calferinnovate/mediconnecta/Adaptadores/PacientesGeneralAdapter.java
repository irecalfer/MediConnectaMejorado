package com.calferinnovate.mediconnecta.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Unidades;
import com.calferinnovate.mediconnecta.R;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Adaptador que pobla el Fragmento GeneralPacientes con los datos de este.
 */
public class PacientesGeneralAdapter {

    private ImageView fotoPaciente;
    private TextInputEditText nombre, apellidos, sexo, dni, lugarNacimiento, edad, fechaNacimiento,
            estadoCivil, fechaIngreso, unidad, habitacion, cipSns, numSeguridadSocial;
    private final Pacientes paciente;
    private final Context context;

    /**
     * Constrctor del adaptador
     */
    public PacientesGeneralAdapter(Pacientes paciente, Context context) {
        this.paciente = paciente;
        this.context = context;
    }

    /**
     * Método que enlaza los recursos de la UI con las variables y setea los datos del paciente.
     *
     * @param view La vista inflada.
     */
    public void rellenaUI(View view) {
        enlazaRecursos(view);
        seteaDatos();
    }

    /**
     * Método que enlaza los recusos de la UI con las variables-
     *
     * @param view La vista inflada.
     */
    public void enlazaRecursos(View view) {
        fotoPaciente = view.findViewById(R.id.fotoPacienteDetalle);
        nombre = view.findViewById(R.id.nombrePaciente);
        apellidos = view.findViewById(R.id.apellidoPaciente);
        sexo = view.findViewById(R.id.sexoPaciente);
        dni = view.findViewById(R.id.dniPaciente);
        lugarNacimiento = view.findViewById(R.id.lugarNacimientoPaciente);
        edad = view.findViewById(R.id.edadPaciente);
        fechaNacimiento = view.findViewById(R.id.fechaNacimientoPaciente);
        estadoCivil = view.findViewById(R.id.estadoCivilPaciente);
        fechaIngreso = view.findViewById(R.id.fechaIngresoPaciente);
        unidad = view.findViewById(R.id.unidadPaciente);
        habitacion = view.findViewById(R.id.habitacionPaciente);
        cipSns = view.findViewById(R.id.cipSnsPaciente);
        numSeguridadSocial = view.findViewById(R.id.numSeguridadSocialPaciente);
    }

    /**
     * Método que actualiza la UI con los datos del paciente seleccionado.
     */
    public void seteaDatos() {
        configuraFotoPacientes();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenHeight = displayMetrics.heightPixels;

        // Porcentaje del alto de la pantalla para el tamaño de texto
        float textPercentage = 0.02f; // Puedes ajustar este valor según tus necesidades

        int desiredTextSize = (int) (screenHeight * textPercentage);

        nombre.setText(paciente.getNombre());
        nombre.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        apellidos.setText(paciente.getApellidos());
        apellidos.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        sexo.setText(paciente.getSexo());
        sexo.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        dni.setText(paciente.getDni());
        dni.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        lugarNacimiento.setText(paciente.getLugarNacimiento());
        lugarNacimiento.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        edad.setText(String.valueOf(calculaEdad(paciente)));
        edad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fechaNacimiento.setText(formateaFecha(paciente.getFechaNacimiento()));
        fechaNacimiento.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        estadoCivil.setText(paciente.getEstadoCivil());
        estadoCivil.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        fechaIngreso.setText(formateaFecha(paciente.getFechaIngreso()));
        fechaIngreso.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        unidad.setText(nombreUnidad(paciente));
        unidad.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        habitacion.setText(String.valueOf(paciente.getFkNumHabitacion()));
        habitacion.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        cipSns.setText(paciente.getCipSns());
        cipSns.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);

        numSeguridadSocial.setText(String.valueOf(paciente.getNumSeguridadSocial()));
        numSeguridadSocial.setTextSize(TypedValue.COMPLEX_UNIT_PX, desiredTextSize);
    }

    private void configuraFotoPacientes(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        int targetWidth = Math.min(screenWidth, 300); // Tamaño máximo
        int targetHeight = Math.min(screenHeight, 300); // Tamaño máximo


        Glide.with(context).load(paciente.getFoto()).circleCrop().override(targetWidth, targetHeight).into(fotoPaciente);
    }
    /**
     * Método usado para calcular la edad del paciente a partir de su fecha de nacimiento.
     *
     * @param pacientes Paciente seleccionado
     * @return La edad del paciente.
     */
    private int calculaEdad(Pacientes pacientes) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNac = LocalDate.parse(pacientes.getFechaNacimiento(), fmt);
        LocalDate ahora = LocalDate.now();

        Period period = Period.between(fechaNac, ahora);
        return period.getYears();
    }

    /**
     * Método que formatea la fecha de nacimiento y de ingreso de yyyy-MM-dd a dd-MM-yyyy
     *
     * @param fecha Fecha a formatear
     * @return fecha formateada
     */
    private String formateaFecha(String fecha) {
        // Formato de entrada (año-mes-día)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Formato de salida (día-mes-año abreviado)
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parsea la fecha de entrada
        LocalDate fechaEntrada = LocalDate.parse(fecha, formatoEntrada);

        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaEntrada.format(formatoSalida);
        return fechaFormateada;
    }


    /**
     * Método que busca la unidad a la que pertenece el paciente y actualiza la UI.
     *
     * @param paciente Paciente seleccionado
     * @return nombre de la unidad.
     */
    private String nombreUnidad(Pacientes paciente) {
        String nombreUnidad = "";
        for (Unidades unidades : ClaseGlobal.getInstance().getListaUnidades()) {
            if (paciente.getFkIdUnidad() == unidades.getId_unidad()) {
                nombreUnidad = unidades.getNombreUnidad();
            }
        }
        return nombreUnidad;
    }
}
