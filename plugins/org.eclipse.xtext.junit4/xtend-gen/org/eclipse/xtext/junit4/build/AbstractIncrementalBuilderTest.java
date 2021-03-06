/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.junit4.build;

import com.google.common.annotations.Beta;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.xtext.Constants;
import org.eclipse.xtext.build.BuildRequest;
import org.eclipse.xtext.build.IncrementalBuilder;
import org.eclipse.xtext.build.IndexState;
import org.eclipse.xtext.build.Source2GeneratedMapping;
import org.eclipse.xtext.generator.OutputConfiguration;
import org.eclipse.xtext.generator.OutputConfigurationAdapter;
import org.eclipse.xtext.generator.OutputConfigurationProvider;
import org.eclipse.xtext.junit4.util.InMemoryURIHandler;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.resource.impl.ChunkedResourceDescriptions;
import org.eclipse.xtext.resource.impl.ProjectDescription;
import org.eclipse.xtext.resource.impl.ResourceDescriptionsData;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure2;
import org.junit.Before;

/**
 * Abstract base class for testing languages in the incremental builder.
 * 
 * @since 2.9
 * 
 * @noreference
 * @noimplement
 */
@Beta
@SuppressWarnings("all")
public abstract class AbstractIncrementalBuilderTest {
  @Inject
  protected IncrementalBuilder incrementalBuilder;
  
  @Inject
  protected IndexState indexState;
  
  @Inject
  protected Provider<XtextResourceSet> resourceSetProvider;
  
  @Inject
  @Named(Constants.LANGUAGE_NAME)
  private String languageName;
  
  @Inject
  private OutputConfigurationProvider configurationProvider;
  
  protected Multimap<URI, URI> generated;
  
  protected List<URI> deleted;
  
  protected List<Issue> issues;
  
  protected InMemoryURIHandler inMemoryURIHandler;
  
  @Before
  public void setUp() {
    this.clean();
    InMemoryURIHandler _inMemoryURIHandler = new InMemoryURIHandler();
    this.inMemoryURIHandler = _inMemoryURIHandler;
  }
  
  protected List<Issue> clean() {
    List<Issue> _xblockexpression = null;
    {
      ArrayListMultimap<URI, URI> _create = ArrayListMultimap.<URI, URI>create();
      this.generated = _create;
      ArrayList<URI> _newArrayList = CollectionLiterals.<URI>newArrayList();
      this.deleted = _newArrayList;
      ArrayList<Issue> _newArrayList_1 = CollectionLiterals.<Issue>newArrayList();
      _xblockexpression = this.issues = _newArrayList_1;
    }
    return _xblockexpression;
  }
  
  protected IndexState build(final BuildRequest buildRequest) {
    this.clean();
    final Function1<URI, IResourceServiceProvider> _function = new Function1<URI, IResourceServiceProvider>() {
      @Override
      public IResourceServiceProvider apply(final URI it) {
        IResourceServiceProvider.Registry _languages = AbstractIncrementalBuilderTest.this.getLanguages();
        return _languages.getResourceServiceProvider(it);
      }
    };
    IncrementalBuilder.Result _build = this.incrementalBuilder.build(buildRequest, _function);
    IndexState _indexState = _build.getIndexState();
    this.indexState = _indexState;
    return this.indexState;
  }
  
  protected void withOutputConfig(final BuildRequest it, final Procedure1<? super OutputConfiguration> init) {
    Set<OutputConfiguration> _outputConfigurations = this.configurationProvider.getOutputConfigurations();
    final OutputConfiguration config = IterableExtensions.<OutputConfiguration>head(_outputConfigurations);
    init.apply(config);
    Pair<String, Set<OutputConfiguration>> _mappedTo = Pair.<String, Set<OutputConfiguration>>of(this.languageName, Collections.<OutputConfiguration>unmodifiableSet(CollectionLiterals.<OutputConfiguration>newHashSet(config)));
    final OutputConfigurationAdapter adapter = new OutputConfigurationAdapter(Collections.<String, Set<OutputConfiguration>>unmodifiableMap(CollectionLiterals.<String, Set<OutputConfiguration>>newHashMap(_mappedTo)));
    XtextResourceSet _resourceSet = it.getResourceSet();
    EList<Adapter> _eAdapters = _resourceSet.eAdapters();
    _eAdapters.add(adapter);
  }
  
  protected abstract IResourceServiceProvider.Registry getLanguages();
  
  protected BuildRequest newBuildRequest(final Procedure1<? super BuildRequest> init) {
    BuildRequest _buildRequest = new BuildRequest();
    final Procedure1<BuildRequest> _function = new Procedure1<BuildRequest>() {
      @Override
      public void apply(final BuildRequest it) {
        ResourceDescriptionsData _resourceDescriptions = AbstractIncrementalBuilderTest.this.indexState.getResourceDescriptions();
        final ResourceDescriptionsData newIndex = _resourceDescriptions.copy();
        URI _uri = AbstractIncrementalBuilderTest.this.uri("");
        it.setBaseDir(_uri);
        XtextResourceSet _get = AbstractIncrementalBuilderTest.this.resourceSetProvider.get();
        final Procedure1<XtextResourceSet> _function = new Procedure1<XtextResourceSet>() {
          @Override
          public void apply(final XtextResourceSet it) {
            URIConverter _uRIConverter = it.getURIConverter();
            EList<URIHandler> _uRIHandlers = _uRIConverter.getURIHandlers();
            _uRIHandlers.clear();
            URIConverter _uRIConverter_1 = it.getURIConverter();
            EList<URIHandler> _uRIHandlers_1 = _uRIConverter_1.getURIHandlers();
            _uRIHandlers_1.add(AbstractIncrementalBuilderTest.this.inMemoryURIHandler);
            ClassLoader _classLoader = AbstractIncrementalBuilderTest.class.getClassLoader();
            it.setClasspathURIContext(_classLoader);
            ProjectDescription _projectDescription = new ProjectDescription();
            final Procedure1<ProjectDescription> _function = new Procedure1<ProjectDescription>() {
              @Override
              public void apply(final ProjectDescription it) {
                it.setName("test-project");
              }
            };
            final ProjectDescription projectDescription = ObjectExtensions.<ProjectDescription>operator_doubleArrow(_projectDescription, _function);
            projectDescription.attachToEmfObject(it);
            Map<String, ResourceDescriptionsData> _emptyMap = CollectionLiterals.<String, ResourceDescriptionsData>emptyMap();
            final ChunkedResourceDescriptions index = new ChunkedResourceDescriptions(_emptyMap, it);
            String _name = projectDescription.getName();
            index.setContainer(_name, newIndex);
          }
        };
        XtextResourceSet _doubleArrow = ObjectExtensions.<XtextResourceSet>operator_doubleArrow(_get, _function);
        it.setResourceSet(_doubleArrow);
        it.setDirtyFiles(Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList()));
        it.setDeletedFiles(Collections.<URI>unmodifiableList(CollectionLiterals.<URI>newArrayList()));
        final BuildRequest.IPostValidationCallback _function_1 = new BuildRequest.IPostValidationCallback() {
          @Override
          public boolean afterValidate(final URI uri, final Iterable<Issue> issues) {
            Iterables.<Issue>addAll(AbstractIncrementalBuilderTest.this.issues, issues);
            return IterableExtensions.isEmpty(issues);
          }
        };
        it.setAfterValidate(_function_1);
        final Procedure1<URI> _function_2 = new Procedure1<URI>() {
          @Override
          public void apply(final URI it) {
            AbstractIncrementalBuilderTest.this.deleted.add(it);
          }
        };
        it.setAfterDeleteFile(_function_2);
        final Procedure2<URI, URI> _function_3 = new Procedure2<URI, URI>() {
          @Override
          public void apply(final URI source, final URI target) {
            AbstractIncrementalBuilderTest.this.generated.put(source, target);
          }
        };
        it.setAfterGenerateFile(_function_3);
        Source2GeneratedMapping _fileMappings = AbstractIncrementalBuilderTest.this.indexState.getFileMappings();
        Source2GeneratedMapping _copy = _fileMappings.copy();
        IndexState _indexState = new IndexState(newIndex, _copy);
        it.setState(_indexState);
      }
    };
    final BuildRequest result = ObjectExtensions.<BuildRequest>operator_doubleArrow(_buildRequest, _function);
    init.apply(result);
    return result;
  }
  
  protected URI delete(final URI uri) {
    try {
      Map<Object, Object> _emptyMap = CollectionLiterals.<Object, Object>emptyMap();
      this.inMemoryURIHandler.delete(uri, _emptyMap);
      return uri;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected URI uri(final String path) {
    return URI.createURI(((InMemoryURIHandler.SCHEME + ":/") + path));
  }
  
  protected URI operator_minus(final String path, final String content) {
    try {
      final URI uri = this.uri(path);
      Map<Object, Object> _emptyMap = CollectionLiterals.<Object, Object>emptyMap();
      OutputStream _createOutputStream = this.inMemoryURIHandler.createOutputStream(uri, _emptyMap);
      final Procedure1<OutputStream> _function = new Procedure1<OutputStream>() {
        @Override
        public void apply(final OutputStream it) {
          try {
            byte[] _bytes = content.getBytes();
            it.write(_bytes);
            it.close();
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        }
      };
      ObjectExtensions.<OutputStream>operator_doubleArrow(_createOutputStream, _function);
      return uri;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  protected boolean containsSuffix(final Iterable<? extends URI> uris, final String... suffixes) {
    final Function1<String, Boolean> _function = new Function1<String, Boolean>() {
      @Override
      public Boolean apply(final String suffix) {
        final Function1<URI, Boolean> _function = new Function1<URI, Boolean>() {
          @Override
          public Boolean apply(final URI uri) {
            String _string = uri.toString();
            return Boolean.valueOf(_string.endsWith(suffix));
          }
        };
        return Boolean.valueOf(IterableExtensions.exists(uris, _function));
      }
    };
    return IterableExtensions.<String>forall(((Iterable<String>)Conversions.doWrapArray(suffixes)), _function);
  }
}
