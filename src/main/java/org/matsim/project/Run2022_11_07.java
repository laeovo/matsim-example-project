package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

public class Run2022_11_07 {
    public static void main(String[] args) {
        Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");
        Scenario scenario = ScenarioUtils.loadScenario(config);

        Controler controler = new Controler(scenario);
        final AbstractModule module = new AbstractModule() {
            @Override public void install() {
                this.bindMobsim().toInstance(null);
            }
        };

        controler.addOverridingModule(module);
        controler.run();
    }
}
