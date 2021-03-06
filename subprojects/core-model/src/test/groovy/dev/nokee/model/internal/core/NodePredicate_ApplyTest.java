package dev.nokee.model.internal.core;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static dev.nokee.model.internal.core.ModelPath.path;
import static dev.nokee.model.internal.core.ModelTestActions.doSomething;
import static dev.nokee.model.internal.core.ModelTestActions.doSomethingElse;
import static dev.nokee.model.internal.core.ModelTestUtils.node;
import static dev.nokee.model.internal.core.NodePredicate.allDirectDescendants;
import static dev.nokee.model.internal.core.NodePredicate.self;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

class NodePredicate_ApplyTest {
	@Test
	void canApplyPredicateToModelActionWhichCanBeScopedToAnyPaths() {
		val paths = new ArrayList<ModelPath>();
		val scopedAction = allDirectDescendants().apply(node -> paths.add(node.getPath())).scope(path("foo"));
		scopedAction.execute(node("foo.bar"));
		scopedAction.execute(node("bar"));

		assertThat(paths, contains(path("foo.bar")));
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkEquals() {
		new EqualsTester()
			.addEqualityGroup(allDirectDescendants().apply(doSomething()), allDirectDescendants().apply(doSomething()))
			.addEqualityGroup(allDirectDescendants().apply(doSomethingElse()))
			.addEqualityGroup(self().apply(doSomething()))
			.testEquals();
	}

	@Test
	@SuppressWarnings("UnstableApiUsage")
	void checkNulls() throws NoSuchMethodException {
		new NullPointerTester().testMethod(allDirectDescendants(), NodePredicate.class.getMethod("apply", ModelAction.class));
	}
}
