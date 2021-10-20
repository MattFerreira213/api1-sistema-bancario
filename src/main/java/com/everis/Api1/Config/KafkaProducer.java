package com.everis.Api1.Config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaProducer {

//    public void EnviarDadosClienteSaque(Long numeroConta) throws ExecutionException, InterruptedException {
//        var producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(properties());
//        var value = numeroConta.toString();
//        var record = new ProducerRecord<>("BANK_NEW_SAQUE", value, value);
//        producer.send(record, (data, ex) -> {
//            if (ex != null) {
//                ex.printStackTrace();
//                return;
//            }
//            //observer
//            System.out.println("Mensagem enviada com sucesso: " + data.topic() + ":::partition: " + numeroConta + data.partition() + "/offset: " + data.offset() + "/timestamp: " + data.timestamp());
//        }).get();
//    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
