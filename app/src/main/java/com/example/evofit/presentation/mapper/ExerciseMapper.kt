package com.example.evofit.presentation.mapper

import com.example.evofit.R

object ExerciseMapper {
    /**
     * Mapeia o ID do exercício para um recurso de imagem.
     * Cada exercício tem sua própria entrada para facilitar a personalização individual das imagens.
     */
    fun toImageRes(exerciseId: String): Int {
        return when (exerciseId) {
            // Costas
            "1" -> R.drawable.img_back
            "2" -> R.drawable.img_back
            "3" -> R.drawable.img_back
            "4" -> R.drawable.img_back
            "5" -> R.drawable.img_back
            "6" -> R.drawable.img_back
            "7" -> R.drawable.img_back
            "8" -> R.drawable.img_back
            "9" -> R.drawable.img_back
            "10" -> R.drawable.img_back

            // Peito
            "11" -> R.drawable.img_chest
            "12" -> R.drawable.img_chest
            "13" -> R.drawable.img_chest
            "14" -> R.drawable.img_chest
            "15" -> R.drawable.img_chest
            "16" -> R.drawable.img_chest
            "17" -> R.drawable.img_chest
            "18" -> R.drawable.img_chest
            "19" -> R.drawable.img_chest
            "20" -> R.drawable.img_chest

            // Pernas
            "21" -> R.drawable.img_legs
            "22" -> R.drawable.img_legs
            "23" -> R.drawable.img_legs
            "24" -> R.drawable.img_legs
            "25" -> R.drawable.img_legs
            "26" -> R.drawable.img_legs
            "27" -> R.drawable.img_legs
            "28" -> R.drawable.img_legs
            "29" -> R.drawable.img_legs
            "30" -> R.drawable.img_legs
            "31" -> R.drawable.img_legs

            // Braços
            "32" -> R.drawable.img_arms
            "33" -> R.drawable.img_arms
            "34" -> R.drawable.img_arms
            "35" -> R.drawable.img_arms
            "36" -> R.drawable.img_arms
            "37" -> R.drawable.img_arms
            "38" -> R.drawable.img_arms
            "39" -> R.drawable.img_arms
            "40" -> R.drawable.img_arms
            "41" -> R.drawable.img_arms
            "42" -> R.drawable.img_arms

            // Ombros
            "43" -> R.drawable.img_shoulder
            "44" -> R.drawable.img_shoulder
            "45" -> R.drawable.img_shoulder
            "46" -> R.drawable.img_elevacao_frontal // Elevação Frontal (Específica)
            "47" -> R.drawable.img_shoulder
            "48" -> R.drawable.img_shoulder
            "49" -> R.drawable.img_shoulder
            "50" -> R.drawable.img_shoulder
            "51" -> R.drawable.img_shoulder

            // Core
            "52" -> R.drawable.img_abs
            "53" -> R.drawable.img_abs
            "54" -> R.drawable.img_abs
            "55" -> R.drawable.img_abs
            "56" -> R.drawable.img_abs
            "57" -> R.drawable.img_abs
            "58" -> R.drawable.img_abs
            "59" -> R.drawable.img_abs
            "60" -> R.drawable.img_abs
            "61" -> R.drawable.img_abs

            // Cardio
            "62" -> R.drawable.img_cardio
            "63" -> R.drawable.img_cardio
            "64" -> R.drawable.img_cardio
            "65" -> R.drawable.img_cardio
            "66" -> R.drawable.img_cardio
            "67" -> R.drawable.img_cardio
            "68" -> R.drawable.img_cardio
            "69" -> R.drawable.img_cardio
            "70" -> R.drawable.img_cardio
            "71" -> R.drawable.img_cardio

            // Gluteo
            "72" -> R.drawable.img_gluteus
            "73" -> R.drawable.img_gluteus
            "74" -> R.drawable.img_gluteus
            "75" -> R.drawable.img_gluteus
            "76" -> R.drawable.img_gluteus
            "77" -> R.drawable.img_gluteus
            "78" -> R.drawable.img_gluteus
            "79" -> R.drawable.img_gluteus
            "80" -> R.drawable.img_gluteus
            "81" -> R.drawable.img_gluteus

            // Panturrilha
            "82" -> R.drawable.img_calf
            "83" -> R.drawable.img_calf
            "84" -> R.drawable.img_calf
            "85" -> R.drawable.img_calf
            "86" -> R.drawable.img_calf
            "87" -> R.drawable.img_calf
            "88" -> R.drawable.img_calf

            else -> R.drawable.img_cardio
        }
    }
}
