package net.mehvahdjukaar.heartstone;

import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;

public class HeartstoneClient {

    public static void init() {
        ClientPlatformHelper.addParticleRegistration(HeartstoneClient::registerParticles);
    }

    private static void registerParticles(ClientPlatformHelper.ParticleEvent event) {
        event.register(Heartstone.HEARTSTONE_PARTICLE.get(), HeartstoneParticle.Factory::new);
        event.register(Heartstone.HEARTSTONE_PARTICLE_EMITTER.get(), HeartstoneParticle.EmitterFactory::new);
    }

    public static void setup() {
    }


}
