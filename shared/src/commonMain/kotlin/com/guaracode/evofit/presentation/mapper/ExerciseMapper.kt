package com.guaracode.evofit.presentation.mapper

import evofit.shared.generated.resources.*
import evofit.shared.generated.resources.Res
import org.jetbrains.compose.resources.DrawableResource

object ExerciseMapper {
    fun toImageRes(exerciseId: String): DrawableResource {
        return when (exerciseId) {
            // Back (1-10, 92-94, 105)
            "1" -> Res.drawable.img_puxada_alta
            "2" -> Res.drawable.img_barra_fixa
            "3" -> Res.drawable.img_remada_curvada_2
            "4" -> Res.drawable.img_remada_unilateral_1
            "5" -> Res.drawable.img_remada_baixa
            "6" -> Res.drawable.img_pullover
            "7" -> Res.drawable.img_remada_cavalinho
            "8" -> Res.drawable.img_pull_down
            "9" -> Res.drawable.img_remada_t_bar
            "10" -> Res.drawable.img_levantamento_terra
            "92" -> Res.drawable.img_remada_articulada
            "93" -> Res.drawable.img_remada_articulada_baixa
            "94" -> Res.drawable.img_puxada_alta_triangulo
            "105" -> Res.drawable.img_remada_articulada_alta_2

            // Chest (11-20, 95-96, 106-107)
            "11" -> Res.drawable.img_supino_reto
            "12" -> Res.drawable.img_supino_inclinado
            "13" -> Res.drawable.img_supino_declinado
            "14" -> Res.drawable.img_crucifixo
            "15" -> Res.drawable.img_crucifixo_inclinado
            "16" -> Res.drawable.img_peck_deck
            "17" -> Res.drawable.img_crossover_2
            "18" -> Res.drawable.img_flexao_de_braco
            "19" -> Res.drawable.img_chest_press
            "20" -> Res.drawable.img_chest_press
            "95" -> Res.drawable.img_supino_reto_halter
            "96" -> Res.drawable.img_supino_reto_articulado
            "106" -> Res.drawable.img_supino_inclinado_articulado
            "107" -> Res.drawable.img_supino_declinado_articulado

            // Legs (21-31, 98-100, 108, 110)
            "21" -> Res.drawable.img_agachamento_livre
            "22" -> Res.drawable.img_leg_press
            "23" -> Res.drawable.img_hack_squat
            "24" -> Res.drawable.img_cadeira_extensora
            "25" -> Res.drawable.img_mesa_flexora
            "26" -> Res.drawable.img_cadeira_flexora
            "27" -> Res.drawable.img_afundo
            "28" -> Res.drawable.img_passada
            "29" -> Res.drawable.img_agachamento_bulgaro
            "30" -> Res.drawable.img_stiff
            "31" -> Res.drawable.img_stiff
            "98" -> Res.drawable.img_flexao_joelhos_maquina
            "99" -> Res.drawable.img_sumo_maquina
            "100" -> Res.drawable.img_agachamento_sumo
            "108" -> Res.drawable.img_cadeira_adutora
            "110" -> Res.drawable.img_agachamento_smith

            // Biceps (32-38, 101)
            "32" -> Res.drawable.img_rosca_direta
            "33" -> Res.drawable.img_rosca_direta_pulley
            "34" -> Res.drawable.img_rosca_alternada
            "35" -> Res.drawable.img_rosca_martelo
            "36" -> Res.drawable.img_rosca_martelo_pulley
            "37" -> Res.drawable.img_rosca_scott
            "38" -> Res.drawable.img_rosca_concentrada
            "101" -> Res.drawable.img_rosca_inclinada_halter

            // Triceps (39-43, 102-104, 109)
            "39" -> Res.drawable.img_triceps_pulley
            "40" -> Res.drawable.img_triceps_pulley_corda
            "41" -> Res.drawable.img_triceps_frances
            "42" -> Res.drawable.img_triceps_testa
            "43" -> Res.drawable.img_paralela_2
            "102" -> Res.drawable.img_triceps_coice
            "103" -> Res.drawable.img_triceps_banco
            "104" -> Res.drawable.img_extensao_triceps_maquina
            "109" -> Res.drawable.img_triceps_press

            // Forearms (44-45)
            "44" -> Res.drawable.img_rosca_inversa
            "45" -> Res.drawable.img_flexao_de_punho

            // Shoulders (46-54)
            "46" -> Res.drawable.img_desenvolvimento_militar
            "47" -> Res.drawable.img_desenvolvimento_halter
            "48" -> Res.drawable.img_elevacao_lateral
            "49" -> Res.drawable.img_elevacao_frontal
            "50" -> Res.drawable.img_crucifixo_inverso
            "51" -> Res.drawable.img_face_pull
            "52" -> Res.drawable.img_desenvolvimento_maquina
            "53" -> Res.drawable.img_remada_alta
            "54" -> Res.drawable.img_arnold_press

            // Core (55-64)
            "55" -> Res.drawable.img_abdominal_tradicional
            "56" -> Res.drawable.img_abdominal_infra
            "57" -> Res.drawable.img_prancha
            "58" -> Res.drawable.img_prancha_lateral
            "59" -> Res.drawable.img_elevacao_pernas
            "60" -> Res.drawable.img_abdominal_bicicleta
            "61" -> Res.drawable.img_abdominal_maquina
            "62" -> Res.drawable.img_crunch_polia
            "63" -> Res.drawable.img_russian_twist
            "64" -> Res.drawable.img_mountain_climber

            // Cardio (65-74)
            "65" -> Res.drawable.img_esteira_caminhada
            "66" -> Res.drawable.img_esteira_corrida
            "67" -> Res.drawable.img_bicicleta_ergometrica
            "68" -> Res.drawable.img_eliptico
            "69" -> Res.drawable.img_escada
            "70" -> Res.drawable.img_remo
            "71" -> Res.drawable.img_pular_corda
            "72" -> Res.drawable.img_corrida_externa
            "73" -> Res.drawable.img_esteira_caminhada
            "74" -> Res.drawable.img_hiit

            // Glutes (75-84, 97)
            "75" -> Res.drawable.img_elevacao_pelvica
            "76" -> Res.drawable.img_elevacao_pelvica
            "77" -> Res.drawable.img_coice_polia
            "78" -> Res.drawable.img_abducao_quadril
            "79" -> Res.drawable.img_agachamento_sumo
            "80" -> Res.drawable.img_stiff
            "81" -> Res.drawable.img_afundo
            "82" -> Res.drawable.img_passada
            "83" -> Res.drawable.img_gluteo_maquina
            "84" -> Res.drawable.img_ponte_gluteo
            "97" -> Res.drawable.img_cadeira_abdutora

            // Calves (85-91)
            "85" -> Res.drawable.img_panturrilha_em_pe
            "86" -> Res.drawable.img_panturrilha_sentado
            "87" -> Res.drawable.img_panturrilha_leg_press
            "88" -> Res.drawable.img_panturrilha_smith
            "89" -> Res.drawable.img_panturrilha_unilateral
            "90" -> Res.drawable.img_panturrilha_hack
            "91" -> Res.drawable.img_panturrilha_degrau

            else -> Res.drawable.ic_cardio
        }
    }
}
