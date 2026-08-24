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
            "1" -> R.drawable.img_puxada_alta
            "2" -> R.drawable.img_barra_fixa
            "3" -> R.drawable.img_remada_curvada_2
            "4" -> R.drawable.img_remada_unilateral_1
            "5" -> R.drawable.img_remada_baixa
            "6" -> R.drawable.img_pull_down
            "7" -> R.drawable.img_remada_cavalinho
            "8" -> R.drawable.img_pull_down
            "9" -> R.drawable.img_remada_t_bar
            "10" -> R.drawable.img_levantamento_terra

            // Peito
            "11" -> R.drawable.img_supino_reto
            "12" -> R.drawable.img_supino_inclinado
            "13" -> R.drawable.img_supino_declinado
            "14" -> R.drawable.img_crucifixo
            "15" -> R.drawable.img_crucifixo_inclinado
            "16" -> R.drawable.img_peck_deck
            "17" -> R.drawable.img_crossover
            "18" -> R.drawable.img_flexao_de_braco
            "19" -> R.drawable.img_chest_press
            "20" -> R.drawable.img_chest_press

            // Pernas
            "21" -> R.drawable.img_agachamento_livre
            "22" -> R.drawable.img_leg_press
            "23" -> R.drawable.img_hack_squat
            "24" -> R.drawable.img_cadeira_extensora
            "25" -> R.drawable.img_mesa_flexora
            "26" -> R.drawable.img_cadeira_flexora
            "27" -> R.drawable.img_afundo
            "28" -> R.drawable.img_passada
            "29" -> R.drawable.img_agachamento_bulgaro
            "30" -> R.drawable.img_stiff
            "31" -> R.drawable.img_stiff

            // Braços
            "32" -> R.drawable.img_rosca_direta
            "33" -> R.drawable.img_rosca_alternada
            "34" -> R.drawable.img_rosca_martelo
            "35" -> R.drawable.img_rosca_scott
            "36" -> R.drawable.img_rosca_concentrada
            "37" -> R.drawable.img_triceps_pulley
            "38" -> R.drawable.img_triceps_frances
            "39" -> R.drawable.img_triceps_testa
            "40" -> R.drawable.img_paralela_2
            "41" -> R.drawable.img_rosca_inversa
            "42" -> R.drawable.img_flexao_de_punho

            // Ombros
            "43" -> R.drawable.img_desenvolvimento_militar
            "44" -> R.drawable.img_desenvolvimento_halter
            "45" -> R.drawable.img_elevacao_lateral
            "46" -> R.drawable.img_elevacao_frontal
            "47" -> R.drawable.img_crucifixo_inverso
            "48" -> R.drawable.img_face_pull
            "49" -> R.drawable.img_desenvolvimento_maquina
            "50" -> R.drawable.img_remada_alta
            "51" -> R.drawable.img_arnold_press

            // Core
            "52" -> R.drawable.img_abdominal_tradicional
            "53" -> R.drawable.img_abdominal_infra
            "54" -> R.drawable.img_prancha
            "55" -> R.drawable.img_prancha_lateral
            "56" -> R.drawable.img_elevacao_pernas
            "57" -> R.drawable.img_abdominal_bicicleta
            "58" -> R.drawable.img_abdominal_maquina
            "59" -> R.drawable.img_crunch_polia
            "60" -> R.drawable.img_russian_twist
            "61" -> R.drawable.img_mountain_climber

            // Cardio
            "62" -> R.drawable.img_esteira_caminhada
            "63" -> R.drawable.img_esteira_corrida
            "64" -> R.drawable.img_bicicleta_ergometrica
            "65" -> R.drawable.img_eliptico
            "66" -> R.drawable.img_escada
            "67" -> R.drawable.img_remo
            "68" -> R.drawable.img_pular_corda
            "69" -> R.drawable.img_corrida_externa
            "70" -> R.drawable.img_esteira_caminhada
            "71" -> R.drawable.img_hiit

            // Gluteo
            "72" -> R.drawable.img_hip_thrust
            "73" -> R.drawable.img_hip_thrust
            "74" -> R.drawable.img_coice_polia
            "75" -> R.drawable.img_abducao_quadril
            "76" -> R.drawable.img_agachamento_sumo
            "77" -> R.drawable.img_stiff
            "78" -> R.drawable.img_afundo
            "79" -> R.drawable.img_passada
            "80" -> R.drawable.img_gluteus
            "81" -> R.drawable.img_gluteus

            // Panturrilha
            "82" -> R.drawable.img_panturrilha_em_pe
            "83" -> R.drawable.img_panturrilha_sentado
            "84" -> R.drawable.img_calf
            "85" -> R.drawable.img_calf
            "86" -> R.drawable.img_panturrilha_unilateral
            "87" -> R.drawable.img_panturrilha_hack
            "88" -> R.drawable.img_panturrilha_escada

            // Novos Braços
            "89" -> R.drawable.img_rosca_direta_pulley
            "90" -> R.drawable.img_rosca_martelo_pulley
            "91" -> R.drawable.img_triceps_pulley_corda

            else -> R.drawable.img_cardio
        }
    }
}
