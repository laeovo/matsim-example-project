package org.matsim.project;

import com.google.inject.Inject;
import com.google.inject.Module;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.*;
import org.matsim.core.controler.corelisteners.ControlerDefaultCoreListenersModule;
import org.matsim.core.events.EventsManagerModule;
import org.matsim.core.mobsim.DefaultMobsimModule;
import org.matsim.core.replanning.StrategyManagerModule;
import org.matsim.core.replanning.annealing.ReplanningAnnealer;
import org.matsim.core.router.TripRouterModule;
import org.matsim.core.router.costcalculators.TravelDisutilityModule;
import org.matsim.core.scenario.ScenarioByInstanceModule;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.scoring.functions.CharyparNagelScoringFunctionModule;
import org.matsim.core.trafficmonitoring.TravelTimeCalculatorModule;
import org.matsim.core.utils.timing.TimeInterpretationModule;

public class Run2022_10_31 {
    public static void main(String[] args) {
        Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");
        config.controler().setLastIteration(2);
        Scenario scenario = ScenarioUtils.loadScenario(config);

        Module module = new AbstractModule() {
            @Override public void install() {
//                bind(Abc.class).to(AbcImpl.class);
//                bind(Helper.class).to(HelperImpl.class);
                install(new NewControlerModule());
                install(new ControlerDefaultCoreListenersModule());
                install(new ScenarioByInstanceModule(scenario));

                install(new EventsManagerModule());
                install(new DefaultMobsimModule());
                install(new TravelTimeCalculatorModule());
                install(new TravelDisutilityModule());
                install(new CharyparNagelScoringFunctionModule());
                install(new TripRouterModule());
                install(new StrategyManagerModule());
                install(new TimeInterpretationModule());
                if (getConfig().replanningAnnealer().isActivateAnnealingModule()) {
                    addControlerListenerBinding().to(ReplanningAnnealer.class);
                }
            }
        };

        var injector = Injector.createInjector(config, module);

//        Abc abc = injector.getInstance(Abc.class);
//        abc.doSomething();

        ControlerI controlerI = injector.getInstance(ControlerI.class);
        controlerI.run();

        Controler controler = new Controler(scenario);
        controler.addOverridingModule(new AbstractModule() {
            @Override public void install() {
                bind(Abc.class).to(AbcImpl.class);
            }
        });
    }

    // ABC
    interface Abc {
        void doSomething();
    }

    private static class AbcImpl implements Abc {
        @Inject
        private Helper helper;

        @Override public void doSomething() {
            System.out.println("abc does something");
            helper.help();
        }
    }

    private static class AbcImpl2 implements Abc {
        @Override public void doSomething() {
            System.out.println("abc2 does something");
        }
    }

    // HELPER
    interface Helper {
        void help();
    }

    private static class HelperImpl implements Helper {
        @Override
        public void help() {
            System.out.println("Calling help method in HelperImpl");
        }
    }
}
